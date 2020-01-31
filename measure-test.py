from sparkmeasure import StageMetrics
from pyspark.sql import SparkSession
import sys

spark = SparkSession.builder.master('yarn').getOrCreate()

stagemetrics = StageMetrics(spark)

stagemetrics.begin()
spark.sql(sys.argv[1]).show()
stagemetrics.end()

# print report to standard output
stagemetrics.print_report()

# save session metrics data in json format (default)
df = stagemetrics.create_stagemetrics_DF("PerfStageMetrics")
stagemetrics.save_data(df.orderBy("jobId", "stageId"), "/tmp/stagemetrics_test1")

aggregatedDF = stagemetrics.aggregate_stagemetrics_DF("PerfStageMetrics")
stagemetrics.save_data(aggregatedDF, "/tmp/stagemetrics_report_test2")