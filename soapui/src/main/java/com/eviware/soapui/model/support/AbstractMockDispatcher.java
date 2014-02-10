/*
 *  SoapUI, copyright (C) 2004-2012 smartbear.com
 *
 *  SoapUI is free software; you can redistribute it and/or modify it under the
 *  terms of version 2.1 of the GNU Lesser General Public License as published by 
 *  the Free Software Foundation.
 *
 *  SoapUI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */

package com.eviware.soapui.model.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eviware.soapui.impl.wsdl.mock.DispatchException;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockResult;
import com.eviware.soapui.model.mock.MockDispatcher;
import com.eviware.soapui.model.mock.MockResult;
import com.eviware.soapui.model.mock.MockRunner;
import org.apache.commons.collections.list.TreeList;

import java.util.Collections;
import java.util.List;

public abstract class AbstractMockDispatcher implements MockDispatcher
{

	private final List<WsdlMockResult> mockResults = Collections.synchronizedList( new TreeList() );
	private long maxResults = 100;
	private int removed = 0;
	private boolean logEnabled = true;


	public MockResult dispatchGetRequest( HttpServletRequest request, HttpServletResponse response )
			throws DispatchException
	{
		throw new DispatchException( "Unsupported HTTP Method: GET" );
	}

	public MockResult dispatchPostRequest( HttpServletRequest request, HttpServletResponse response )
			throws DispatchException
	{
		throw new DispatchException( "Unsupported HTTP Method: POST" );
	}

	public MockResult dispatchHeadRequest( HttpServletRequest request, HttpServletResponse response )
			throws DispatchException
	{
		throw new DispatchException( "Unsupported HTTP Method: HEAD" );
	}

	public MockResult dispatchPutRequest( HttpServletRequest request, HttpServletResponse response )
			throws DispatchException
	{
		throw new DispatchException( "Unsupported HTTP Method: PUT" );
	}

	public MockResult dispatchDeleteRequest( HttpServletRequest request, HttpServletResponse response )
			throws DispatchException
	{
		throw new DispatchException( "Unsupported HTTP Method: DELETE" );
	}

	public MockResult dispatchPatchRequest( HttpServletRequest request, HttpServletResponse response )
			throws DispatchException
	{
		throw new DispatchException( "Unsupported HTTP Method: PATCH" );
	}

	public MockResult dispatchRequest( HttpServletRequest request, HttpServletResponse response )
			throws DispatchException
	{
		String method = request.getMethod();

		if( method.equals( "POST" ) )
			return dispatchPostRequest( request, response );
		else if( method.equals( "GET" ) )
			return dispatchGetRequest( request, response );
		else if( method.equals( "HEAD" ) )
			return dispatchHeadRequest( request, response );
		else if( method.equals( "PUT" ) )
			return dispatchPutRequest( request, response );
		else if( method.equals( "DELETE" ) )
			return dispatchDeleteRequest( request, response );
		else if( method.equals( "PATCH" ) )
			return dispatchPatchRequest( request, response );

		throw new DispatchException( "Unsupported HTTP Method: " + method );
	}

	public synchronized void addMockResult( WsdlMockResult mockResult )
	{
		if( maxResults > 0 && logEnabled )
			mockResults.add( mockResult );

		while( mockResults.size() > maxResults )
		{
			mockResults.remove( 0 );
			removed++;
		}
	}

	public MockResult getMockResultAt( int index )
	{
		return index <= removed ? null : mockResults.get( index - removed );
	}

	public int getMockResultCount()
	{
		return mockResults.size() + removed;
	}

	public synchronized void clearResults()
	{
		mockResults.clear();
	}

	public long getMaxResults()
	{
		return maxResults;
	}

	public synchronized void setMaxResults( long maxNumberOfResults )
	{
		this.maxResults = maxNumberOfResults;

		while( mockResults.size() > maxNumberOfResults )
		{
			mockResults.remove( 0 );
			removed++;
		}
	}

	public void setLogEnabled( boolean logEnabled )
	{
		this.logEnabled = logEnabled;
	}


}
