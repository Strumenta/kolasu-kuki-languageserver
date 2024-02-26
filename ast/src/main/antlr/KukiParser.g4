parser grammar KukiParser;

options {
	tokenVocab = KukiLexer;
}

recipe: title+=ID+ INGREDIENTS FOR yield=NUMBER ingredients+=ingredient+ UTENSILS utensils+=utensil* STEPS steps+=step+;

ingredient: HYPHEN? name=itemDeclaration (COLON amount=NUMBER unit=UNIT?)?;

utensil: HYPHEN? name=itemDeclaration;

step: (number=NUMBER PERIOD)? action PERIOD;

action: creation | temperature | time | space | singular;

creation: name=(MIX|SLICE) items=itemList INTO THE? target=itemDeclaration;
temperature: name=HEAT items=itemList TO amount=NUMBER unit=TEMPERATURE_UNIT;
time: name=(BAKE|COOK) items=itemList FOR amount=NUMBER unit=TIME_UNIT;
space: name=(PLACE|ADD|SERVE) items=itemList (IN|TO) THE? target=itemReference;
singular: name=(PEEL|BEAT|FLIP|DRAIN|GRATE) items=itemList;

itemDeclaration: ID+;
itemReference: ID+;
itemList: THE? items+=itemReference ((COMMA THE? items+=itemReference)* AND THE? items+=itemReference)?;
