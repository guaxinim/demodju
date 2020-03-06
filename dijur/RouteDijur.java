import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
//import org.apache.camel.component.properties.PropertiesComponent;


public class RouteDijur extends RouteBuilder {

    @Override
    public void configure() {

	//PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
	//pc.setLocation("file:/tmp/arquivo.properties");

	restConfiguration()
			.contextPath("/")
			.component("undertow")
			.host("0.0.0.0")
			.port("8080")
			.enableCORS(true)
            .corsHeaderProperty("Access-Control-Allow-Origin","*")
            .corsHeaderProperty("Access-Control-Allow-Headers","Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
		
		rest("/api")
			.post("/receive")
				.to("direct:api")
			.get("/teste")
				.to("direct:teste");

	
	//getContext().setTracing(true);
	//from("file:/tmp/pdfs?charset=utf-8&allowNullBody=false&delay=500")
	from("direct:api")
	.log("${body}")
	.convertBodyTo(String.class)
	.process(new RegexProcessor())
	.to("elasticsearch-rest://elastic?hostAddresses=ec2-18-229-239-234.sa-east-1.compute.amazonaws.com:9200&operation=Index&indexName=dijur&enableSSL=false&user=admin&password=admin&lazyStartProducer=true");

	from("direct:teste")
	.log("${body}");

	}

	public class RegexProcessor implements Processor  {
	
		@Override
		public void process(Exchange exchange) throws Exception {
			
			String body = (String) exchange.getIn().getBody();
			//System.out.println("--------------");
			//System.out.println(body);
			System.out.println("--------------");
			Map<String, Object> props = new HashMap<String, Object>();
			System.out.println(exchange.getProperties());
			props.put("PROCESSO", System.getenv("PROCESSO"));
			props.put("ASSUNTO", System.getenv("ASSUNTO"));
			props.put("AUTOR", System.getenv("AUTOR"));
			props.put("REU", System.getenv("REU"));
			props.put("CLASSE", System.getenv("CLASSE"));
			props.put("TIPO", System.getenv("TIPO"));
			props.put("AJUIZADO", System.getenv("AJUIZADO"));
			props.put("DECIDO", System.getenv("DECIDO"));
			props.put("JULGAMENTO", System.getenv("JULGAMENTO"));
			
			System.out.println("--------------");
			
			props.forEach((k,v)->{
				
				if (v != null) {
					String value = String.valueOf(v);
					System.out.println("ORIGINAL: " + v);
					int i = value.indexOf("%");
					String inicio = value.substring(0, i);
					System.out.println("INICIO: " + inicio);
					String fim = value.substring(i+1, value.length());
					System.out.println("FIM: " + fim);
					String pattern = "(" + inicio + ")" + "(.+?)" + fim + "(.*)";
					System.out.println("PATTERN: " + pattern);

					Pattern padrao1 = Pattern.compile(pattern, Pattern.DOTALL);
					Matcher mat1 = padrao1.matcher(body);
					while (mat1.find()) {
						//System.out.println(k + " - PATTERN: " + v);
						System.out.println(k + ": " + mat1.group(2));
						props.replace(k, mat1.group(2));
					}
				}
			});

			props.put("DOCUMENTO", exchange.getIn().getHeader("arquivo"));
			exchange.getIn().setBody(props);
		}
	}
}
