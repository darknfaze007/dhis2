package org.hisp.dhis.dxf2.webmessage;

/*
 * Copyright (c) 2004-2014, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.Objects;
import org.hisp.dhis.common.DxfNamespaces;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
@JacksonXmlRootElement( localName = "webMessage", namespace = DxfNamespaces.DXF_2_0 )
@JsonPropertyOrder( {
    "status", "code", "httpStatusCode", "message", "devMessage", "response"
} )
public class WebMessage
{
    /**
     * Message status, currently two statuses are available: OK, ERROR.
     *
     * @see WebMessageStatus
     */
    protected WebMessageStatus status;

    /**
     * Internal code for this message. Should be used to help with third party clients which
     * should not have to resort to string parsing of message to know what is happening.
     */
    protected Integer code;

    /**
     * HTTP status code.
     */
    protected Integer httpStatusCode;

    /**
     * Non-technical message, should be simple and could possibly be used to display message
     * to an end-user.
     */
    protected String message;

    /**
     * Technical message that should explain as much details as possible, mainly to be used
     * for debugging.
     */
    protected String devMessage;

    /**
     * When a simple text feedback is not enough, you can use this interface to implement your
     * own message responses.
     *
     * @see WebMessageResponse
     */
    protected WebMessageResponse response;

    public WebMessage()
    {
        this.status = WebMessageStatus.OK;
    }

    public WebMessage( WebMessageStatus status )
    {
        this.status = status;
    }

    public WebMessage( WebMessageStatus status, Integer httpStatusCode )
    {
        this.status = status;
        this.httpStatusCode = httpStatusCode;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public WebMessageStatus getStatus()
    {
        return status;
    }

    public void setStatus( WebMessageStatus status )
    {
        this.status = status;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public Integer getCode()
    {
        return code;
    }

    public void setCode( Integer code )
    {
        this.code = code;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public Integer getHttpStatusCode()
    {
        return httpStatusCode;
    }

    public void setHttpStatusCode( Integer httpStatusCode )
    {
        this.httpStatusCode = httpStatusCode;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDevMessage()
    {
        return devMessage;
    }

    public void setDevMessage( String devMessage )
    {
        this.devMessage = devMessage;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public WebMessageResponse getResponse()
    {
        return response;
    }

    public void setResponse( WebMessageResponse response )
    {
        this.response = response;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper( this )
            .add( "status", status )
            .add( "code", code )
            .add( "httpStatusCode", httpStatusCode )
            .add( "message", message )
            .add( "devMessage", devMessage )
            .add( "response", response )
            .toString();
    }
}
