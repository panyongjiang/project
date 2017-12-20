package com.uway.mobile.util;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.uway.mobile.domain.Param;

public class ConverterUtil implements Converter{

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(Param.class);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Param method = (Param)value;
        if (method!=null) {
            writer.addAttribute("attr", method.getAttr());
            writer.setValue(method.getContent());;
        }
		
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext arg1) {
		Param method = new Param();
        String attr = reader.getAttribute("attr");
        method.setAttr(attr);;
      
        String content = reader.getValue();
      
        method.setContent(content);;
       
        return method;
	}

}
