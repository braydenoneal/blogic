import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Whence;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Consumer;

/**
 * Reimplementation of {@link net.minecraft.client.gui.components.MultilineTextField}
 * A multiline edit box with support for basic keyboard shortcuts.
 */
@Environment(EnvType.CLIENT)
public class EditBox {
    public static final int UNLIMITED_LENGTH = Integer.MAX_VALUE;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int CURSOR_WIDTH = 2;
    private final Font textRenderer;
    private final List<Substring> lines = Lists.newArrayList();
    private final int width;
    private final int lineHeight;
    private String text;
    private int cursor;
    private int selectionEnd;
    private boolean selecting;
    private final int maxLength = Integer.MAX_VALUE;
    private final int maxLines = Integer.MAX_VALUE;
    private final Consumer<String> changeListener = text -> {
    };
    private Runnable cursorChangeListener = () -> {
    };

    public EditBox(Font textRenderer, int width, int lineHeight) {
        this.textRenderer = textRenderer;
        this.width = width;
        this.lineHeight = lineHeight;
        this.setText("");
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public boolean hasMaxLength() {
        return this.maxLength != Integer.MAX_VALUE;
    }

    public boolean hasMaxLines() {
        return this.maxLines != Integer.MAX_VALUE;
    }

    public void setCursorChangeListener(Runnable cursorChangeListener) {
        this.cursorChangeListener = cursorChangeListener;
    }

    public void setText(String text, boolean allowOverflow) {
        String string = this.truncateForReplacement(text);
        if (allowOverflow || !this.exceedsMaxLines(string)) {
            this.text = string;
//            this.cursor = this.text.length();
            this.cursor = 0;
            this.selectionEnd = this.cursor;
            this.onChange();
        }
    }

    public String getText() {
        return this.text;
    }

    public void setText(String setText) {
        this.setText(setText, false);
    }

    public void replaceSelection(String string) {
        if (!string.isEmpty() || this.hasSelection()) {
            String string2 = this.truncate(StringUtil.filterText(string, true));
            Substring substring = this.getSelection();
            String string3 = new StringBuilder(this.text).replace(substring.beginIndex, substring.endIndex, string2).toString();
            if (!this.exceedsMaxLines(string3)) {
                this.text = string3;
                this.cursor = substring.beginIndex + string2.length();
                this.selectionEnd = this.cursor;
                this.onChange();
            }
        }
    }

    public void delete(int offset) {
        if (!this.hasSelection()) {
            this.selectionEnd = Mth.clamp(this.cursor + offset, 0, this.text.length());
        }

        this.replaceSelection("");
    }

    public int getCursor() {
        return this.cursor;
    }

    public void setSelecting(boolean selecting) {
        this.selecting = selecting;
    }

    public Substring getSelection() {
        return new Substring(Math.min(this.selectionEnd, this.cursor), Math.max(this.selectionEnd, this.cursor));
    }

    public int getLineCount() {
        return this.lines.size();
    }

    public int getCurrentLineIndex() {
        for (int i = 0; i < this.lines.size(); i++) {
            Substring substring = this.lines.get(i);
            if (this.cursor >= substring.beginIndex && this.cursor <= substring.endIndex) {
                return i;
            }
        }

        return -1;
    }

    public Substring getLine(int index) {
        return this.lines.get(Mth.clamp(index, 0, this.lines.size() - 1));
    }

    public void moveCursor(Whence movement, int amount) {
        switch (movement) {
            case ABSOLUTE:
                this.cursor = amount;
                break;
            case RELATIVE:
                this.cursor += amount;
                break;
            case END:
                this.cursor = this.text.length() + amount;
        }

        this.cursor = Mth.clamp(this.cursor, 0, this.text.length());
        this.cursorChangeListener.run();
        if (!this.selecting) {
            this.selectionEnd = this.cursor;
        }
    }

    public void moveCursorLine(int offset) {
        if (offset != 0) {
            int i = this.textRenderer.width(this.text.substring(this.getCurrentLine().beginIndex, this.cursor)) + 2;
            Substring substring = this.getOffsetLine(offset);
            int j = this.textRenderer.plainSubstrByWidth(this.text.substring(substring.beginIndex, substring.endIndex), i).length();
            this.moveCursor(Whence.ABSOLUTE, substring.beginIndex + j);
        }
    }

    public void moveCursor(double x, double y) {
        int i = Mth.floor(x);
        int j = Mth.floor(y / lineHeight);
        Substring substring = this.lines.get(Mth.clamp(j, 0, this.lines.size() - 1));
        int k = this.textRenderer.plainSubstrByWidth(this.text.substring(substring.beginIndex, substring.endIndex), i).length();
        this.moveCursor(Whence.ABSOLUTE, substring.beginIndex + k);
    }

    public void selectWord() {
        EditBox.Substring substring = this.getPreviousWordAtCursor();
        this.moveCursor(Whence.ABSOLUTE, substring.beginIndex);
        this.setSelecting(true);
        this.moveCursor(Whence.ABSOLUTE, substring.endIndex);
    }

    public boolean handleSpecialKey(KeyEvent key) {
        this.selecting = key.hasShiftDown();
        if (key.isSelectAll()) {
            this.cursor = this.text.length();
            this.selectionEnd = 0;
            return true;
        } else if (key.isCopy()) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getSelectedText());
            return true;
        } else if (key.isPaste()) {
            this.replaceSelection(Minecraft.getInstance().keyboardHandler.getClipboard());
            return true;
        } else if (key.isCut()) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getSelectedText());
            this.replaceSelection("");
            return true;
        } else {
            return switch (key.key()) {
                case 257, 335 -> {
                    this.replaceSelection("\n");
                    yield true;
                }
                case 259 -> {
                    if (key.hasControlDownWithQuirk()) {
                        Substring substring = this.getPreviousWordAtCursor();
                        this.delete(substring.beginIndex - this.cursor);
                    } else {
                        this.delete(-1);
                    }

                    yield true;
                }
                case 261 -> {
                    if (key.hasControlDownWithQuirk()) {
                        Substring substring = this.getNextWordAtCursor();
                        this.delete(substring.beginIndex - this.cursor);
                    } else {
                        this.delete(1);
                    }

                    yield true;
                }
                case 262 -> {
                    if (key.hasControlDownWithQuirk()) {
                        Substring substring = this.getNextWordAtCursor();
                        this.moveCursor(Whence.ABSOLUTE, substring.beginIndex);
                    } else {
                        this.moveCursor(Whence.RELATIVE, 1);
                    }

                    yield true;
                }
                case 263 -> {
                    if (key.hasControlDownWithQuirk()) {
                        Substring substring = this.getPreviousWordAtCursor();
                        this.moveCursor(Whence.ABSOLUTE, substring.beginIndex);
                    } else {
                        this.moveCursor(Whence.RELATIVE, -1);
                    }

                    yield true;
                }
                case 264 -> {
                    if (!key.hasControlDownWithQuirk()) {
                        this.moveCursorLine(1);
                    }

                    yield true;
                }
                case 265 -> {
                    if (!key.hasControlDownWithQuirk()) {
                        this.moveCursorLine(-1);
                    }

                    yield true;
                }
                case 266 -> {
                    this.moveCursor(Whence.ABSOLUTE, 0);
                    yield true;
                }
                case 267 -> {
                    this.moveCursor(Whence.END, 0);
                    yield true;
                }
                case 268 -> {
                    if (key.hasControlDownWithQuirk()) {
                        this.moveCursor(Whence.ABSOLUTE, 0);
                    } else {
                        this.moveCursor(Whence.ABSOLUTE, this.getCurrentLine().beginIndex);
                    }

                    yield true;
                }
                case 269 -> {
                    if (key.hasControlDownWithQuirk()) {
                        this.moveCursor(Whence.END, 0);
                    } else {
                        this.moveCursor(Whence.ABSOLUTE, this.getCurrentLine().endIndex);
                    }

                    yield true;
                }
                default -> false;
            };
        }
    }

    public Iterable<Substring> getLines() {
        return this.lines;
    }

    public boolean hasSelection() {
        return this.selectionEnd != this.cursor;
    }

    @VisibleForTesting
    public String getSelectedText() {
        Substring substring = this.getSelection();
        return this.text.substring(substring.beginIndex, substring.endIndex);
    }

    private Substring getCurrentLine() {
        return this.getOffsetLine(0);
    }

    private Substring getOffsetLine(int offsetFromCurrent) {
        int i = this.getCurrentLineIndex();
        if (i < 0) {
            LOGGER.error("Cursor is not within text (cursor = {}, length = {})", this.cursor, this.text.length());
            return this.lines.getLast();
        } else {
            return this.lines.get(Mth.clamp(i + offsetFromCurrent, 0, this.lines.size() - 1));
        }
    }

    @VisibleForTesting
    public Substring getPreviousWordAtCursor() {
        if (this.text.isEmpty()) {
            return Substring.EMPTY;
        } else {
            int i = Mth.clamp(this.cursor, 0, this.text.length() - 1);

            while (i > 0 && Character.isWhitespace(this.text.charAt(i - 1))) {
                i--;
            }

            while (i > 0 && !Character.isWhitespace(this.text.charAt(i - 1))) {
                i--;
            }

            return new Substring(i, this.getWordEndIndex(i));
        }
    }

    @VisibleForTesting
    public Substring getNextWordAtCursor() {
        if (this.text.isEmpty()) {
            return Substring.EMPTY;
        } else {
            int i = Mth.clamp(this.cursor, 0, this.text.length() - 1);

            while (i < this.text.length() && !Character.isWhitespace(this.text.charAt(i))) {
                i++;
            }

            while (i < this.text.length() && Character.isWhitespace(this.text.charAt(i))) {
                i++;
            }

            return new Substring(i, this.getWordEndIndex(i));
        }
    }

    private int getWordEndIndex(int startIndex) {
        int i = startIndex;

        while (i < this.text.length() && !Character.isWhitespace(this.text.charAt(i))) {
            i++;
        }

        return i;
    }

    private void onChange() {
        this.rewrap();
        this.changeListener.accept(this.text);
        this.cursorChangeListener.run();
    }

    private void rewrap() {
        this.lines.clear();
        if (this.text.isEmpty()) {
            this.lines.add(Substring.EMPTY);
        } else {
            this.textRenderer.getSplitter().splitLines(this.text, this.width, Style.EMPTY, false, (style, start, end) -> this.lines.add(new Substring(start, end)));
            if (this.text.charAt(this.text.length() - 1) == '\n') {
                this.lines.add(new Substring(this.text.length(), this.text.length()));
            }
        }
    }

    private String truncateForReplacement(String value) {
        return this.hasMaxLength() ? StringUtil.truncateStringIfNecessary(value, this.maxLength, false) : value;
    }

    private String truncate(String value) {
        String string = value;
        if (this.hasMaxLength()) {
            int i = this.maxLength - this.text.length();
            string = StringUtil.truncateStringIfNecessary(value, i, false);
        }

        return string;
    }

    private boolean exceedsMaxLines(String text) {
        return this.hasMaxLines() && this.textRenderer.getSplitter().splitLines(text, this.width, Style.EMPTY).size() + (StringUtil.endsWithNewLine(text) ? 1 : 0) > this.maxLines;
    }

    public record Substring(int beginIndex, int endIndex) {
        static final Substring EMPTY = new Substring(0, 0);
    }
}
