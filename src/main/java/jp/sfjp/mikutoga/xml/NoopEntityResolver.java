/*
 * No-operation Entity Resolver for XML.
 *
 * License : The MIT License
 * Copyright(c) 2019 olyutorskii
 */

package jp.sfjp.mikutoga.xml;

import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * No-operation Entity Resolver implementation for preventing XXE.
 *
 * @see <a href="https://en.wikipedia.org/wiki/XML_external_entity_attack">
 *     XML external entity attack (Wikipedia)
 *     </a>
 */
public final class NoopEntityResolver implements EntityResolver{

    /** Singleton resolver. */
    public static final EntityResolver NOOP_RESOLVER =
            new NoopEntityResolver();


    /**
     * Constructor.
     */
    private NoopEntityResolver(){
        super();
        return;
    }


    /**
     * {@inheritDoc}
     *
     * <p>Prevent any external entity reference XXE.
     *
     * @param publicId {@inheritDoc}
     * @param systemId {@inheritDoc}
     * @return empty input source
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId){
        Reader emptyReader = new StringReader("");
        InputSource source = new InputSource(emptyReader);

        source.setPublicId(publicId);
        source.setSystemId(systemId);

        return source;
    }

}
