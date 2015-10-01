insert into crawlslim
(
	select database_project_db2.clim.songId,
    case when date = "2015-01-21" then views end as views121,
    case when date = "2015-01-22" then views end as views122,
    case when date = "2015-01-23" then views end as views123,
    case when date = "2015-01-24" then views end as views124,
    case when date = "2015-02-05" then views end as views205,
    case when date = "2015-02-06" then views end as views206,
    case when date = "2015-02-07" then views end as views207,
    case when date = "2015-02-08" then views end as views208,
    case when date = "2015-02-10" then views end as views210,
    case when date = "2015-02-11" then views end as views211,
    case when date = "2015-02-13" then views end as views213,
    case when date = "2015-02-14" then views end as views214,
    case when date = "2015-02-16" then views end as views216,
    case when date = "2015-02-21" then views end as views221,
    case when date = "2015-02-26" then views end as views226,
    case when date = "2015-02-28" then views end as views228,
	case when date = "2015-03-01" then views end as views301,
	case when date = "2015-03-02" then views end as views302,
	case when date = "2015-03-03" then views end as views303,
	case when date = "2015-03-06" then views end as views306,
	case when date = "2015-03-12" then views end as views312,
	case when date = "2015-03-14" then views end as views314,
	case when date = "2015-03-15" then views end as views315
from clim 
)
