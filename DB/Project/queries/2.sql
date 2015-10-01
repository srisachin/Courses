insert into views2(
select database_project_db2.crawlhistory.songId,views from crawlhistory
    where (date = "2015-02-10" or date = "2015-02-11" or date = "2015-02-13" or date = "2015-02-14" or date = "2015-02-16" or 
date = "2015-02-21" or date = "2015-02-26" or date = "2015-02-28")
)

    