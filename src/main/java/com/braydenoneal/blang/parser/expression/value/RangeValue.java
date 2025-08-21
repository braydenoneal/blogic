package com.braydenoneal.blang.parser.expression.value;

public class RangeValue extends Value<Range> {
    public RangeValue(Range value) {
        super(value);
    }

    @Override
    public String toString() {
        return "range(" + value().start() + ", " + value().end() + ", " + value().step() + ")";
    }
}
