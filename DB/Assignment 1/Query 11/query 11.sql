select L1.BEER, count(*) from LIKES L1 where L1.NAME in (select FREQUENTS.NAME 
from FREQUENTS, SERVES where FREQUENTS.BAR = SERVES.BAR and L1.BEER = SERVES.BEER )
Group by L1.BEER