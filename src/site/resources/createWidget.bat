REM   -DarchetypeRepository=http://repo.51flashmall.com:8081/nexus/content/groups/public
mvn archetype:generate -DarchetypeRepository=http://repo.51flashmall.com:8081/nexus/content/groups/public -DarchetypeGroupId=com.huotu.hotcms -DarchetypeArtifactId=widget-skeleton -DarchetypeVersion=1.0-SNAPSHOT -DgroupId=com.huotu.hotcms.widget.%1 -DartifactId=%2
