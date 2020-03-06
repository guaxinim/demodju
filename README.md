# demo-bb-dijur

## Execução da rota:  

`kamel run -d camel-elasticsearch-rest dijur/RouteDijur.java -e AJUIZADO='ajuizada por[\s]%,' -e PROCESSO='Processo n.:[\s]%\n' -e ASSUNTO='Assunto:[\s]%\n' -e AUTOR='Autor:[\s]%\n' -e REU='R.u:[\s]%\n' -e CLASSE='Classe  Assunto:[\s]%\n' -e TIPO='Trata-se de[\s]%,' -e DECIDO='DECIDO[\n]*%\.\n' -e JULGAMENTO='JULGO[\s]%\s' --dev`

## Chamada

`curl -k -v -XPOST http://route-dijur-dijur.apps.cluster-508d.508d.sandbox1825.opentlc.com/api/receive -H "Content-Type: application/x-www-form-urlencoded; charset=utf-8" --data-urlencode "@SENTENCA.txt"`

