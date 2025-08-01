package com.braydenoneal.data.controller;

import com.braydenoneal.data.controller.function.Context;
import com.braydenoneal.data.controller.function.CustomFunction;
import com.braydenoneal.data.controller.function.types.*;
import com.braydenoneal.data.controller.parameter.types.BooleanParameter;
import com.braydenoneal.data.controller.parameter.types.IntegerParameter;
import com.braydenoneal.data.controller.parameter.types.VoidParameter;
import com.braydenoneal.data.controller.terminal.Terminal;
import com.braydenoneal.data.controller.terminal.types.BooleanTerminal;
import com.braydenoneal.data.controller.terminal.types.IntegerTerminal;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;

import java.util.List;
import java.util.Map;

public class Test {
    public static final CustomFunction CUSTOM_FUNCTION = new CustomFunction(
            "gameOfLife",
            new VoidParameter(),
            Map.of("a", new BooleanParameter(), "b", new IntegerParameter()),
            List.of(
                    new SetVariableFunction(
                            "evenGameTime",
                            Either.right(new EqualsFunction(
                                    Either.right(new ModulusFunction(
                                            Either.right(new GetTimeFunction()),
                                            Either.left(new IntegerTerminal(2))
                                    )),
                                    Either.left(new IntegerTerminal(0))
                            ))
                    ),
                    new SetGlobalVariableFunction(
                            "neighborsCount",
                            Either.right(new ConditionalFunction(
                                    Either.right(new IsBlockAirFunction(
                                            Either.left(new IntegerTerminal(-1)),
                                            Either.left(new IntegerTerminal(2)),
                                            Either.left(new IntegerTerminal(-1))
                                    )),
                                    Either.left(new IntegerTerminal(0)),
                                    Either.left(new IntegerTerminal(1))
                            )),
                            Either.right(new GetVariableFunction("evenGameTime"))
                    ),
                    new SetGlobalVariableFunction(
                            "neighborsCount",
                            Either.right(new AddFunction(
                                    Either.right(new GetGlobalVariableFunction("neighborsCount")),
                                    Either.right(new ConditionalFunction(
                                            Either.right(new IsBlockAirFunction(
                                                    Either.left(new IntegerTerminal(-1)),
                                                    Either.left(new IntegerTerminal(2)),
                                                    Either.left(new IntegerTerminal(0))
                                            )),
                                            Either.left(new IntegerTerminal(0)),
                                            Either.left(new IntegerTerminal(1))
                                    ))
                            )),
                            Either.right(new GetVariableFunction("evenGameTime"))
                    ),
                    new SetGlobalVariableFunction(
                            "neighborsCount",
                            Either.right(new AddFunction(
                                    Either.right(new GetGlobalVariableFunction("neighborsCount")),
                                    Either.right(new ConditionalFunction(
                                            Either.right(new IsBlockAirFunction(
                                                    Either.left(new IntegerTerminal(-1)),
                                                    Either.left(new IntegerTerminal(2)),
                                                    Either.left(new IntegerTerminal(1))
                                            )),
                                            Either.left(new IntegerTerminal(0)),
                                            Either.left(new IntegerTerminal(1))
                                    ))
                            )),
                            Either.right(new GetVariableFunction("evenGameTime"))
                    ),
                    new SetGlobalVariableFunction(
                            "neighborsCount",
                            Either.right(new AddFunction(
                                    Either.right(new GetGlobalVariableFunction("neighborsCount")),
                                    Either.right(new ConditionalFunction(
                                            Either.right(new IsBlockAirFunction(
                                                    Either.left(new IntegerTerminal(0)),
                                                    Either.left(new IntegerTerminal(2)),
                                                    Either.left(new IntegerTerminal(-1))
                                            )),
                                            Either.left(new IntegerTerminal(0)),
                                            Either.left(new IntegerTerminal(1))
                                    ))
                            )),
                            Either.right(new GetVariableFunction("evenGameTime"))
                    ),
                    new SetGlobalVariableFunction(
                            "neighborsCount",
                            Either.right(new AddFunction(
                                    Either.right(new GetGlobalVariableFunction("neighborsCount")),
                                    Either.right(new ConditionalFunction(
                                            Either.right(new IsBlockAirFunction(
                                                    Either.left(new IntegerTerminal(0)),
                                                    Either.left(new IntegerTerminal(2)),
                                                    Either.left(new IntegerTerminal(1))
                                            )),
                                            Either.left(new IntegerTerminal(0)),
                                            Either.left(new IntegerTerminal(1))
                                    ))
                            )),
                            Either.right(new GetVariableFunction("evenGameTime"))
                    ),
                    new SetGlobalVariableFunction(
                            "neighborsCount",
                            Either.right(new AddFunction(
                                    Either.right(new GetGlobalVariableFunction("neighborsCount")),
                                    Either.right(new ConditionalFunction(
                                            Either.right(new IsBlockAirFunction(
                                                    Either.left(new IntegerTerminal(1)),
                                                    Either.left(new IntegerTerminal(2)),
                                                    Either.left(new IntegerTerminal(1))
                                            )),
                                            Either.left(new IntegerTerminal(0)),
                                            Either.left(new IntegerTerminal(1))
                                    ))
                            )),
                            Either.right(new GetVariableFunction("evenGameTime"))
                    ),
                    new SetVariableFunction(
                            "block",
                            Either.right(new ConditionalFunction(
                                    Either.right(new OrFunction(
                                            Either.right(new LessThanFunction(
                                                    Either.right(new GetGlobalVariableFunction("neighborsCount")),
                                                    Either.left(new IntegerTerminal(2))
                                            )),
                                            Either.right(new GreaterThanFunction(
                                                    Either.right(new GetGlobalVariableFunction("neighborsCount")),
                                                    Either.left(new IntegerTerminal(3))
                                            ))
                                    )),
                                    Either.left(new IntegerTerminal(0)),
                                    Either.left(new IntegerTerminal(1))
                            ))
                    ),
                    new SetVariableFunction(
                            "shouldPlaceBlock",
                            Either.right(new AndFunction(
                                    Either.right(new NotFunction(
                                            Either.right(new EqualsFunction(
                                                    Either.right(new GetGlobalVariableFunction("neighborsCount")),
                                                    Either.left(new IntegerTerminal(2))
                                            ))
                                    )),
                                    Either.right(new EqualsFunction(
                                            Either.right(new ModulusFunction(
                                                    Either.right(new GetTimeFunction()),
                                                    Either.left(new IntegerTerminal(2))
                                            )),
                                            Either.left(new IntegerTerminal(1))
                                    ))
                            ))
                    ),
                    new PlaceBlockFunction(
                            Either.left(new IntegerTerminal(0)),
                            Either.left(new IntegerTerminal(2)),
                            Either.left(new IntegerTerminal(0)),
                            Either.right(new GetVariableFunction("block")),
                            Either.right(new GetVariableFunction("shouldPlaceBlock"))
                    )
            )
    );

    public static void test() {
        CustomFunction customFunction = new CustomFunction(
                "gameOfLife",
                new VoidParameter(),
                Map.of(),
                List.of(
                        new SetGlobalVariableFunction(
                                "neighborsCount",
                                Either.right(new AddFunction(
                                        Either.right(new AddFunction(
                                                Either.right(new AddFunction(
                                                        Either.right(new ConditionalFunction(
                                                                Either.right(new IsBlockAirFunction(
                                                                        Either.left(new IntegerTerminal(-1)),
                                                                        Either.left(new IntegerTerminal(2)),
                                                                        Either.left(new IntegerTerminal(-1))
                                                                )),
                                                                Either.left(new IntegerTerminal(0)),
                                                                Either.left(new IntegerTerminal(1))
                                                        )),
                                                        Either.right(new ConditionalFunction(
                                                                Either.right(new IsBlockAirFunction(
                                                                        Either.left(new IntegerTerminal(-1)),
                                                                        Either.left(new IntegerTerminal(2)),
                                                                        Either.left(new IntegerTerminal(0))
                                                                )),
                                                                Either.left(new IntegerTerminal(0)),
                                                                Either.left(new IntegerTerminal(1))
                                                        ))
                                                )),
                                                Either.right(new AddFunction(
                                                        Either.right(new ConditionalFunction(
                                                                Either.right(new IsBlockAirFunction(
                                                                        Either.left(new IntegerTerminal(-1)),
                                                                        Either.left(new IntegerTerminal(2)),
                                                                        Either.left(new IntegerTerminal(1))
                                                                )),
                                                                Either.left(new IntegerTerminal(0)),
                                                                Either.left(new IntegerTerminal(1))
                                                        )),
                                                        Either.right(new ConditionalFunction(
                                                                Either.right(new IsBlockAirFunction(
                                                                        Either.left(new IntegerTerminal(0)),
                                                                        Either.left(new IntegerTerminal(2)),
                                                                        Either.left(new IntegerTerminal(-1))
                                                                )),
                                                                Either.left(new IntegerTerminal(0)),
                                                                Either.left(new IntegerTerminal(1))
                                                        ))
                                                ))
                                        )),
                                        Either.right(new AddFunction(
                                                Either.right(new AddFunction(
                                                        Either.right(new ConditionalFunction(
                                                                Either.right(new IsBlockAirFunction(
                                                                        Either.left(new IntegerTerminal(0)),
                                                                        Either.left(new IntegerTerminal(2)),
                                                                        Either.left(new IntegerTerminal(1))
                                                                )),
                                                                Either.left(new IntegerTerminal(0)),
                                                                Either.left(new IntegerTerminal(1))
                                                        )),
                                                        Either.right(new ConditionalFunction(
                                                                Either.right(new IsBlockAirFunction(
                                                                        Either.left(new IntegerTerminal(1)),
                                                                        Either.left(new IntegerTerminal(2)),
                                                                        Either.left(new IntegerTerminal(-1))
                                                                )),
                                                                Either.left(new IntegerTerminal(0)),
                                                                Either.left(new IntegerTerminal(1))
                                                        ))
                                                )),
                                                Either.right(new AddFunction(
                                                        Either.right(new ConditionalFunction(
                                                                Either.right(new IsBlockAirFunction(
                                                                        Either.left(new IntegerTerminal(1)),
                                                                        Either.left(new IntegerTerminal(2)),
                                                                        Either.left(new IntegerTerminal(0))
                                                                )),
                                                                Either.left(new IntegerTerminal(0)),
                                                                Either.left(new IntegerTerminal(1))
                                                        )),
                                                        Either.right(new ConditionalFunction(
                                                                Either.right(new IsBlockAirFunction(
                                                                        Either.left(new IntegerTerminal(1)),
                                                                        Either.left(new IntegerTerminal(2)),
                                                                        Either.left(new IntegerTerminal(1))
                                                                )),
                                                                Either.left(new IntegerTerminal(0)),
                                                                Either.left(new IntegerTerminal(1))
                                                        ))
                                                ))
                                        ))
                                )),
                                Either.right(new EqualsFunction(
                                        Either.right(new ModulusFunction(
                                                Either.right(new GetTimeFunction()),
                                                Either.left(new IntegerTerminal(2))
                                        )),
                                        Either.left(new IntegerTerminal(0))
                                ))
                        ),
                        new PlaceBlockFunction(
                                Either.left(new IntegerTerminal(0)),
                                Either.left(new IntegerTerminal(2)),
                                Either.left(new IntegerTerminal(0)),
                                Either.right(new ConditionalFunction(
                                        Either.right(new OrFunction(
                                                Either.right(new LessThanFunction(
                                                        Either.right(new GetGlobalVariableFunction("neighborsCount")),
                                                        Either.left(new IntegerTerminal(2))
                                                )),
                                                Either.right(new GreaterThanFunction(
                                                        Either.right(new GetGlobalVariableFunction("neighborsCount")),
                                                        Either.left(new IntegerTerminal(3))
                                                ))
                                        )),
                                        Either.left(new IntegerTerminal(0)),
                                        Either.left(new IntegerTerminal(1))
                                )),
                                Either.right(new AndFunction(
                                        Either.right(new NotFunction(
                                                Either.right(new EqualsFunction(
                                                        Either.right(new GetGlobalVariableFunction("neighborsCount")),
                                                        Either.left(new IntegerTerminal(2))
                                                ))
                                        )),
                                        Either.right(new EqualsFunction(
                                                Either.right(new ModulusFunction(
                                                        Either.right(new GetTimeFunction()),
                                                        Either.left(new IntegerTerminal(2))
                                                )),
                                                Either.left(new IntegerTerminal(1))
                                        ))
                                ))
                        )
                )
        );
        System.out.println(customFunction);

        Terminal terminal = customFunction.call(new Context(null, null, Map.of()), Map.of());
        System.out.println(terminal);

        JsonElement encoded = CustomFunction.CODEC.encodeStart(JsonOps.INSTANCE, customFunction).resultOrPartial().orElseThrow();
        System.out.println(encoded);

        CustomFunction decoded = CustomFunction.CODEC.parse(JsonOps.INSTANCE, encoded).resultOrPartial().orElseThrow();
        System.out.println(decoded);

        JsonElement fromString = JsonParser.parseString("{\"name\":\"customFunction\",\"return_type\":{\"parameter_type\":\"blogic:boolean\"},\"parameter_types\":{\"var\":{\"parameter_type\":\"blogic:boolean\"}},\"body\":[{\"input\":{\"input\":{\"name\":\"var\",\"function_type\":\"blogic:get_variable\"},\"function_type\":\"blogic:not\"},\"function_type\":\"blogic:not\"}]}");
        CustomFunction encodedFromString = CustomFunction.CODEC.parse(JsonOps.INSTANCE, fromString).resultOrPartial().orElseThrow();
        System.out.println(encodedFromString);
        System.out.println(encodedFromString.call(new Context(null, null, Map.of()), Map.of("var", Either.left(new BooleanTerminal(false)))));

        System.exit(0);
    }
}
