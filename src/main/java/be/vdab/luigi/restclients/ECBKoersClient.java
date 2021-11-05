package be.vdab.luigi.restclients;

import be.vdab.luigi.exceptions.KoersClientException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

@Qualifier("ECB")
@Component
public class ECBKoersClient implements KoersClient {
    private final URL url;
    private final XMLInputFactory factory = XMLInputFactory.newInstance();

/*    ECBKoersClient() {
        try {
            url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        } catch (MalformedURLException ex) {
            throw new KoersClientException("ECB URL is verkeerd.", ex);
        }
    }*/

    ECBKoersClient(@Value("${ecbKoersURL}") URL url) {
        this.url = url;
    }

    @Override
    public BigDecimal getDollarKoers() {
        try (var stream = url.openStream()) {
            for (var reader = factory.createXMLStreamReader(stream);
                 reader.hasNext(); ) {
                reader.next();
                if (reader.isStartElement()) {
                    if ("USD".equals(reader.getAttributeValue(0))) {
                        return new BigDecimal(reader.getAttributeValue(1));
                    }
                }
            }
            throw new KoersClientException("XML van ECB geen USD.");
        } catch (IOException | NumberFormatException | XMLStreamException ex) {
            throw new KoersClientException("Kan koers niet lezen via ECB.", ex);
        }
    }
}
