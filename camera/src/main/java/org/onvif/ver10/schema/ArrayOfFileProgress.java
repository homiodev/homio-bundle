package org.onvif.ver10.schema;

import jakarta.xml.bind.annotation.*;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "ArrayOfFileProgress",
        propOrder = {"fileProgress", "extension"})
public class ArrayOfFileProgress {

    @XmlElement(name = "FileProgress")
    protected List<FileProgress> fileProgress;

    
    @Getter @XmlElement(name = "Extension")
    protected ArrayOfFileProgressExtension extension;

    
    @Getter @XmlAnyAttribute
    private final Map<QName, String> otherAttributes = new HashMap<QName, String>();

    
    public List<FileProgress> getFileProgress() {
        if (fileProgress == null) {
            fileProgress = new ArrayList<FileProgress>();
        }
        return this.fileProgress;
    }

    
    public void setExtension(ArrayOfFileProgressExtension value) {
        this.extension = value;
    }

}
