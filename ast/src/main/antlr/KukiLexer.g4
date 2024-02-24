lexer grammar KukiLexer;

INGREDIENTS: 'INGREDIENTS';
UTENSILS: 'UTENSILS';
STEPS: 'STEPS';

THE: 'the';
AND: 'and';
INTO: 'into';
TO: 'to';
FOR: 'for';

HYPHEN: '-';
COLON: ':';
PERIOD: '.';
COMMA: ',';

MIX: 'Mix';
CUT: 'Cut';
ADD: 'Add';
HEAT: 'Heat';
SERVE: 'Serve';
WASH: 'Wash';
PLACE: 'Place';
BAKE: 'Bake';

UNIT: 'ml' | 'g';
TIME_UNIT: 'second' 's'? | 'minute' 's'? | 'hour' 's'?;
TEMPERATURE_UNIT: 'degrees' | 'farenheit';

ID: [a-zA-Z]+;
NUMBER: [0-9]+ ('.' [0-9]+)?;

BLANK: [ \t\r\n]+ -> skip;
