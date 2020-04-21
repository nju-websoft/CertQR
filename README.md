CertQR is a toolkit to find a maximum successful sub-query for semantic association search.
=========================================
/certqr/src contains the code of the whole project.<br>
/certqr/lib contains the libraries that the code is dependent on.<br>
/certqr/example contains an example entity-relation graph.<br>
/certqr/example/ExampleTriples is the source data of the example.<br>
/certqr/certqr.jar is the runnable jar file. To run the example correctly, you have to put the example folder and certqr.jar in the same directory. <br>

To get a result of query relaxation, you may wish to run 'java -jar certqr.jar'

We also provide the simulated queries in the /certqr/query folder, including 460 queries for DBpedia, 195 queries for LinkedMDB, and 285 queries for Mondial. Each file 'X_Y_id.txt' contains one query consisting of Y entities in the X dataset. In the file, each line provides the URI of one entity.

