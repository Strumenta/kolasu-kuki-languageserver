parser grammar KukiParser;

options {
	tokenVocab = KukiLexer;
}

recipe: title=ID INGREDIENTS ingredients+=ingredient+ UTENSILS utensils+=utensil* STEPS steps+=step+;

ingredient: HYPHEN? name=item_declaration COLON amount=NUMBER unit=UNIT?;

utensil: HYPHEN? name=item_declaration;

step: (number=NUMBER PERIOD)? action PERIOD;

action: mix | cut | place | heat | bake;

mix: MIX THE items+=reference ((COMMA items+=reference)* AND items+=reference)? INTO target=item_declaration;
cut: CUT THE items+=reference ((COMMA items+=reference)* AND items+=reference)? INTO target=item_declaration;
place: PLACE THE items+=reference ((COMMA items+=reference)* AND items+=reference)? INTO target=item_declaration;
heat: HEAT THE items+=reference ((COMMA items+=reference)* AND items+=reference)? TO amount=NUMBER unit=TEMPERATURE_UNIT;
bake: BAKE THE items+=reference ((COMMA items+=reference)* AND items+=reference)? FOR amount=NUMBER unit=TIME_UNIT;

reference: ID;
item_declaration: ID;