package com.ebreez.etrcs.traacs.mappers;

import org.springframework.beans.factory.config.AbstractFactoryBean;

public class TraacsMapperFactory extends AbstractFactoryBean<Object>{

	private String traacsMapperClass;
	
	@Override
	protected Object createInstance() throws Exception {
		
		Object aMapper = null;
		if( traacsMapperClass != null )	{
			try	{
				aMapper = Class.forName( traacsMapperClass ).newInstance();
			}
			catch( Exception e ) {
				// Error creating mapper
				System.out.println( "Error creating Mapper..." );
			}
		}
		return aMapper;
	}

	public String getTraacsMapperClass() {
		return traacsMapperClass;
	}

	public void setTraacsMapperClass(String traacsMapperClass) {
		this.traacsMapperClass = traacsMapperClass;
	}
		
	@Override
	public Class<?> getObjectType() {
		@SuppressWarnings("rawtypes")
		Class aMapper = TraacsBaseMapper.class;
		try	{
			aMapper = Class.forName( traacsMapperClass );
		}
		catch( Exception e ) {
			//do nothing!
		}
		return aMapper;
	}
}
