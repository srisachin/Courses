select distinct F1.NAME
from FREQUENTS F1
where F1.NAME not in
        (select F2.NAME
         from FREQUENTS F2
         where not exists (select * from SERVES S, LIKES L
                           where F2.BAR=S.BAR and F2.NAME=L.NAME
                             and S.BEER = L.BEER))
