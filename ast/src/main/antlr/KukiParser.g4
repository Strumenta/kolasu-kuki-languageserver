parser grammar KukiParser;

options {
	tokenVocab = KukiLexer;
}

recipe: title=ID INGREDIENTS (ingredients+=ingredient)+ STEPS steps+=step+;

ingredient: HYPHEN? name=ID COLON amount=NUMBER unit=UNIT?;

step: (number=NUMBER PERIOD)? action PERIOD;

action: mix | cut | place | heat | bake;

mix: MIX THE items+=ID ((COMMA items+=ID)* AND items+=ID) INTO target=ID;
cut: CUT THE items+=ID ((COMMA items+=ID)* AND items+=ID) INTO target=ID;
place: PLACE THE items+=ID ((COMMA items+=ID)* AND items+=ID) INTO target=ID;
heat: HEAT THE items+=ID ((COMMA items+=ID)* AND items+=ID) TO amount=NUMBER unit=TEMPERATURE_UNIT;
bake: BAKE THE items+=ID ((COMMA items+=ID)* AND items+=ID) FOR amount=NUMBER time=TIME_UNIT;