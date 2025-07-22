package com.braydenoneal.data.controller.function;

//import com.braydenoneal.data.controller.terminal.Terminal;
//import com.mojang.datafixers.util.Either;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//
//public class Function {
//    Either<Function, Terminal> param;
//
//    public static Codec<Function> codec = Codec.recursive(
//            "function",
//            selfCodec -> RecordCodecBuilder.create(
//                    instance -> instance.group(
//                            Codec.either(
//                                    selfCodec,
//                                    Terminal.TERMINAL_CODEC
//                            ).fieldOf("param").forGetter(Function::getParam)
//                    ).apply(instance, Function::new)
//            )
//    );
//
//    public Function(Either<Function, Terminal> param) {
//        this.param = param;
//    }
//
//    public Either<Function, Terminal> getParam() {
//        return param;
//    }
//}

import com.braydenoneal.data.controller.terminal.Terminal;
import com.mojang.serialization.Codec;

public interface Function {
    Codec<Function> CODEC = FunctionType.REGISTRY.getCodec().dispatch("functionType", Function::getType, FunctionType::codec);

    default Terminal call() {
        return null;
    }

    FunctionType<?> getType();
}
