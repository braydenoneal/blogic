package com.braydenoneal.data.controller.function;

public class TEST {
    public static void main(String[] args) {
//        ReadRedstoneIsHighFunction readRedstoneIsHighFunction = new ReadRedstoneIsHighFunction(Map.of(
//                "x", new IntegerTerminal(0),
//                "y", new IntegerTerminal(0),
//                "z", new IntegerTerminal(0)
//        ));
//
//        NotFunction notFunction = new NotFunction(Map.of(
//                "input", readRedstoneIsHighFunction.call()
//        ));
//
//        WriteRedstoneIsHighFunction writeRedstoneIsHighFunction = new WriteRedstoneIsHighFunction(Map.of(
//                "x", new IntegerTerminal(-2),
//                "y", new IntegerTerminal(0),
//                "z", new IntegerTerminal(0),
//                "high", notFunction.call()
//        ));
//
//        writeRedstoneIsHighFunction.call();

//        Codec<AbstractFunction> abstractFunctionCodec = Codec.recursive(
//                "abstractFunction",
//                selfCodec -> {
//                    return RecordCodecBuilder.create(instance ->
//                            instance.group(
//                                    Codec.STRING.fieldOf("function").forGetter(AbstractFunction::getName),
//                                    Codec.unboundedMap(
//                                            Codec.STRING,
//                                            Codec.either(
//                                                    selfCodec,
//                                                    Codec.unit(new BooleanTerminal(false))
//                                            )
//                                    ).fieldOf("parameters").forGetter(AbstractFunction::getParameters)
//                            ).apply(instance, AbstractFunction::new)
//                    );
//                }
//        );
    }
}
