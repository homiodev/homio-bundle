package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "CodingCapabilities",
        propOrder = {
                "audioEncodingCapabilities",
                "audioDecodingCapabilities",
                "videoDecodingCapabilities",
                "any"
        })
public class CodingCapabilities {

    
    @Getter @XmlElement(name = "AudioEncodingCapabilities")
    protected AudioEncoderConfigurationOptions audioEncodingCapabilities;

    
    @Getter @XmlElement(name = "AudioDecodingCapabilities")
    protected AudioDecoderConfigurationOptions audioDecodingCapabilities;

    
    @Getter @XmlElement(name = "VideoDecodingCapabilities", required = true)
    protected VideoDecoderConfigurationOptions videoDecodingCapabilities;

    @XmlAnyElement(lax = true)
    protected List<java.lang.Object> any;

    
    @Getter @XmlAnyAttribute
    private final Map<QName, String> otherAttributes = new HashMap<QName, String>();

    
    public void setAudioEncodingCapabilities(AudioEncoderConfigurationOptions value) {
        this.audioEncodingCapabilities = value;
    }

    
    public void setAudioDecodingCapabilities(AudioDecoderConfigurationOptions value) {
        this.audioDecodingCapabilities = value;
    }

    
    public void setVideoDecodingCapabilities(VideoDecoderConfigurationOptions value) {
        this.videoDecodingCapabilities = value;
    }

    
    public List<java.lang.Object> getAny() {
        if (any == null) {
            any = new ArrayList<java.lang.Object>();
        }
        return this.any;
    }

}
