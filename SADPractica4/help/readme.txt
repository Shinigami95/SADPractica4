----------------------------------- INSTRUCCIONES -----------------------------------

java -jar getArff.jar -f plain_data -a arff_file -t {0,1,2} [-u]

Las opciones son:

	-h

		Muestra las instrucciones del programa, ignora el resto de opciones.

	-f path

		Se elige en path el fichero o directorio de los datos en bruto.

	-a path

		Se elige en path el fichero donde se guardará el arff resultante.

	-u

		Sirve para indicar que sea sin supervisar (por defecto supervisado)

	-t i

		Se indica en i el tipo de fichero en bruto:

			0 -> Directorio (pelis)

			1 -> Texto plano (sms)

			2 -> Csv (tweets)

			otro -> Da error

-------------------------------------------------------------------------------------

Ejemplos

java -jar getArff.jar -f movies_reviews/train -a movie.arff -t 0
java -jar getArff.jar -f movies_reviews/test_blind -a movie2.arff -t 0 -u

java -jar getArff.jar -f sms_spam/SMS_SpamCollection.train.txt -a spam.arff -t 1
java -jar getArff.jar -f sms_spam/SMS_SpamCollection.test_blind.txt -a spam2.arff -t 1 -u

java -jar getArff.jar -f tweet/tweetSentiment.train.csv -a tweet.arff -t 2
java -jar getArff.jar -f tweet/tweetSentiment.test_blind.csv -a tweet2.arff -t 2 -u