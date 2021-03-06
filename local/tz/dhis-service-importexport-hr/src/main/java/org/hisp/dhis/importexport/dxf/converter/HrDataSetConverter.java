package org.hisp.dhis.importexport.dxf.converter;

/*
 * Copyright (c) 2004-2012, University of Oslo, University Of Dar es salaam
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
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

import java.util.Collection;
import java.util.Map;

import org.amplecode.quick.BatchHandler;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;
import org.hisp.dhis.hr.HrDataSet;
import org.hisp.dhis.hr.HrDataSetService;
import org.hisp.dhis.importexport.HrExportParams;
import org.hisp.dhis.importexport.ImportHrObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLHrConverter;

import org.hisp.dhis.importexport.importer.HrDataSetImporter;


/**
 * @author John Francis Mukulu <john.f.mukulu@gmail.com>
 * @version $Id$
 */
public class HrDataSetConverter
    extends HrDataSetImporter implements XMLHrConverter
{
    public static final String COLLECTION_NAME = "forms";
    public static final String ELEMENT_NAME = "form";
    
    private static final String FIELD_FORMID = "formId";
    private static final String FIELD_FORMNAME = "formName";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_HYPERTEXT = "hyptertext";
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public HrDataSetConverter( HrDataSetService hrDataSetService )
    {
    	this.hrDataSetService = hrDataSetService;
    }
    
    /**
     * Constructor for read operations.
     * 
     * @param batchHandler the batchHandler to use.
     * @param importObjectService the importObjectService to use.
     * @param categoryService the dataElementCategoryService to use.
     */
    public HrDataSetConverter( BatchHandler<HrDataSet> batchHandler, 
        ImportHrObjectService importObjectService,
        HrDataSetService hrDataSetService )
    {
        this.batchHandler = batchHandler;
        this.importObjectService = importObjectService;
        this.hrDataSetService = hrDataSetService;
    }

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------

    public void write( XMLWriter writer, HrExportParams params )
    {
        Collection<HrDataSet> hrDataSets = hrDataSetService.getAllHrDataSets();
        
        if ( hrDataSets != null && hrDataSets.size() > 0 )
        {
            writer.openElement( COLLECTION_NAME );
            
            for ( HrDataSet hrDataSet : hrDataSets )
            {
                writer.openElement( ELEMENT_NAME );
                
                writer.writeElement( FIELD_FORMID, String.valueOf( hrDataSet.getId() ) );
                writer.writeElement( FIELD_FORMNAME, hrDataSet.getName() );
                writer.writeElement( FIELD_DESCRIPTION, hrDataSet.getDescription() );
                writer.writeElement( FIELD_HYPERTEXT, hrDataSet.getHypertext() );
                writer.closeElement();
            }
            
            writer.closeElement();
        }
    }
    
    public void read( XMLReader reader, ImportParams params )
    {
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            final Map<String, String> values = reader.readElements( ELEMENT_NAME );
            
            final HrDataSet hrDataSet = new HrDataSet();
            
            hrDataSet.setId( Integer.parseInt( values.get( FIELD_FORMID ) ) );
            hrDataSet.setName( values.get( FIELD_FORMNAME ) );
            hrDataSet.setDescription( values.get( FIELD_DESCRIPTION ) );
            hrDataSet.setHypertext(values.get(FIELD_HYPERTEXT)) ;
            importObject( hrDataSet, params );
        }
    }
}
