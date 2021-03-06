package handling.mina;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

@Singleton
public class MapleCodecFactory implements ProtocolCodecFactory {

    private final MaplePacketEncoder encoder;
    private final MaplePacketDecoder decoder;

    @Inject
    public MapleCodecFactory(MaplePacketEncoder encoder, MaplePacketDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public ProtocolEncoder getEncoder() throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder() throws Exception {
        return decoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
}
