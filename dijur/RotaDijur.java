import org.apache.camel.builder.RouteBuilder;

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

public class RotaDijur extends RouteBuilder {

    @Override
    public void configure() {
	
		getContext().setTracing(true);
        from("file:/tmp/pdfs?charset=utf-8&allowNullBody=false&delay=500")
	    //.convertBodyTo(String.class)
	    //.setBody(body().regexReplaceAll("\\n", ""))
	    .convertBodyTo(String.class)
	    .process(new RegexProcessor())
		.to("elasticsearch-rest://elastic?hostAddresses=172.30.247.44:9200&operation=Index&indexName=dijur&enableSSL=false&user=kibana&password=kibana");
		//.to("log:foo");
    }

	public class RegexProcessor implements Processor  {
	
		@Override
		public void process(Exchange exchange) throws Exception {
			
			String body = (String) exchange.getIn().getBody();
			Map<String, Object> props = new HashMap<String, Object>();
			//props.putAll(exchange.getProperties());
			props.put("PROCESSO", System.getenv("PROCESSO"));
			props.put("ASSUNTO", System.getenv("ASSUNTO"));
			props.put("AUTOR", System.getenv("AUTOR"));
			props.put("REU", System.getenv("REU"));
			props.put("TIPO", System.getenv("TIPO"));
			props.put("AJUIZADO", System.getenv("AJUIZADO"));
			props.put("EMFACE", System.getenv("EMFACE"));
			props.put("DECIDO", System.getenv("DECIDO"));
			props.put("JULGAMENTO", System.getenv("JULGAMENTO"));
			
			System.out.println("--------------");
			
			props.forEach((k,v)->{
				System.out.println("Item:" + k + " Value:"+ v);
				if (v != null) {
					String value = String.valueOf(v);
					int i = value.indexOf("%");
					String inicio = value.substring(0, i-1);
					String fim = value.substring(i+1, value.length());
					String pattern = "(" + inicio + ")" + "(.+?)" + fim + "(.*)";
					System.out.println("PATTERN: " + pattern);
					
					Pattern padrao1 = Pattern.compile(pattern, Pattern.DOTALL);
					Matcher mat1 = padrao1.matcher(body);
					while (mat1.find()) {
						System.out.println("GRUPO: " + mat1.group(2));
					}
				}
			});

			exchange.getIn().setBody(props);
		}
	}
}