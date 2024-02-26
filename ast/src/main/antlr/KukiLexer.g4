lexer grammar KukiLexer;

INGREDIENTS: 'INGREDIENTS';
UTENSILS: 'UTENSILS';
STEPS: 'STEPS';

THE: 'the';
AND: 'and';
INTO: 'into';
TO: 'to';
FOR: 'for';
IN: 'in';
WITH: 'with';

HYPHEN: '-';
COLON: ':';
PERIOD: '.';
COMMA: ',';

MIX: 'Mix';
ADD: 'Add';
HEAT: 'Heat';
SERVE: 'Serve';
WASH: 'Wash';
PLACE: 'Place';
BAKE: 'Bake';
PEEL: 'Peel';
SLICE: 'Slice';
COOK: 'Cook';
BEAT: 'Beat';
FLIP: 'Flip';
GRATE: 'Grate';
DRAIN: 'Drain';

UNIT: 'ml' | 'g';
TIME_UNIT: 'seconds' | 'minutes' | 'hours';
TEMPERATURE_UNIT: 'degrees' | 'farenheit';

ID: [a-zA-Z]+;
NUMBER: [0-9]+ ('.' [0-9]+)?;

BLANK: [ \t\r\n]+ -> skip;
