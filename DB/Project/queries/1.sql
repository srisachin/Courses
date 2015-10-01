select database_project_db2.crawlhistory.songId,
    case when (date = "2015-01-21" or date = "2015-01-22" or date = "2015-01-23" or date = "2015-01-24") then views end as views1,
    case when (date = "2015-02-05" or date = "2015-02-06" or date = "2015-02-07" or date = "2015-02-08") then views end as views2,
    case when (date = "2015-02-10" or date = "2015-02-11" or date = "2015-02-13" or date = "2015-02-14" or date = "2015-02-16") then views end as views3,
    case when (date = "2015-02-21" or date = "2015-02-26" or date = "2015-02-28") then views end as views4,
    case when (date = "2015-03-01" or date = "2015-03-02" or date = "2015-03-03" or date = "2015-03-06") then views end as views5,
    case when (date = "2015-03-12" or date = "2015-03-14" or date = "2015-03-15") then views end as views6
from crawlhistory