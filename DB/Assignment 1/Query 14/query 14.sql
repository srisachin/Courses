select distinct SERVES.BAR
from SERVES
where SERVES.BAR not in
(
select distinct SERVES.BAR
from SERVES
where not exists (select * from FREQUENTS, LIKES 
                  where FREQUENTS.BAR= SERVES.BAR and FREQUENTS.NAME= LIKES.NAME
                  and SERVES.BEER = LIKES.BEER)
)