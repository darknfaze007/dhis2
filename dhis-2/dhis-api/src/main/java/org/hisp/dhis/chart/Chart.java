package org.hisp.dhis.chart;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hisp.dhis.common.AnalyticsType;
import org.hisp.dhis.common.DimensionalObject;
import org.hisp.dhis.common.DxfNamespaces;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.NameableObject;
import org.hisp.dhis.common.view.DetailedView;
import org.hisp.dhis.common.view.DimensionalView;
import org.hisp.dhis.common.view.ExportView;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.user.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author Lars Helge Overland
 */
@JacksonXmlRootElement( localName = "chart", namespace = DxfNamespaces.DXF_2_0 )
public class Chart
    extends BaseChart
{
    private static final long serialVersionUID = 2570074075484545534L;

    private String series;

    private String category;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Chart()
    {
        setAutoFields();
    }

    public Chart( String name )
    {
        this();
        this.name = name;
    }

    // -------------------------------------------------------------------------
    // Init
    // -------------------------------------------------------------------------

    @Override
    public void init( User user, Date date, OrganisationUnit organisationUnit,
        List<OrganisationUnit> organisationUnitsAtLevel, List<OrganisationUnit> organisationUnitsInGroups,
        I18nFormat format )
    {
        this.user = user;
        this.relativePeriodDate = date;
        this.relativeOrganisationUnit = organisationUnit;
        this.organisationUnitsAtLevel = organisationUnitsAtLevel;
        this.organisationUnitsInGroups = organisationUnitsInGroups;
        this.format = format;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    @Override
    public List<NameableObject> series()
    {
        DimensionalObject object = getDimensionalObject( series, relativePeriodDate, user, true,
            organisationUnitsAtLevel, organisationUnitsInGroups, format );

        return object != null ? object.getItems() : null;
    }

    @Override
    public List<NameableObject> category()
    {
        DimensionalObject object = getDimensionalObject( category, relativePeriodDate, user, true,
            organisationUnitsAtLevel, organisationUnitsInGroups, format );

        return object != null ? object.getItems() : null;
    }

    @Override
    public void populateAnalyticalProperties()
    {
        columns.addAll( getDimensionalObjectList( series ) );
        rows.addAll( getDimensionalObjectList( category ) );

        for ( String filter : filterDimensions )
        {
            filters.addAll( getDimensionalObjectList( filter ) );
        }
    }

    public List<OrganisationUnit> getAllOrganisationUnits()
    {
        if ( transientOrganisationUnits != null && !transientOrganisationUnits.isEmpty() )
        {
            return transientOrganisationUnits;
        }
        else
        {
            return organisationUnits;
        }
    }

    public OrganisationUnit getFirstOrganisationUnit()
    {
        List<OrganisationUnit> units = getAllOrganisationUnits();
        return units != null && !units.isEmpty() ? units.iterator().next() : null;
    }

    public List<Period> getAllPeriods()
    {
        List<Period> list = new ArrayList<>();

        list.addAll( relativePeriods );

        for ( Period period : periods )
        {
            if ( !list.contains( period ) )
            {
                list.add( period );
            }
        }

        return list;
    }

    /**
     * Sets all dimensions for this chart.
     * 
     * @param series the series dimension.
     * @param category the category dimension.
     * @param filter the filter dimension.
     */
    public void setDimensions( String series, String category, String filter )
    {
        this.series = series;
        this.category = category;
        this.filterDimensions.clear();
        this.filterDimensions.add( filter );
    }

    @Override
    public AnalyticsType getAnalyticsType()
    {
        return AnalyticsType.AGGREGATE;
    }

    public int getWidth()
    {
        return 700;
    }

    public int getHeight()
    {
        return 500;
    }

    // -------------------------------------------------------------------------
    // Getters and setters properties
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getSeries()
    {
        return series;
    }

    public void setSeries( String series )
    {
        this.series = series;
    }

    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getCategory()
    {
        return category;
    }

    public void setCategory( String category )
    {
        this.category = category;
    }

    @Override
    @JsonProperty
    @JsonView( { DetailedView.class, ExportView.class, DimensionalView.class } )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isRewindRelativePeriods()
    {
        return rewindRelativePeriods;
    }

    @Override
    public void setRewindRelativePeriods( boolean rewindRelativePeriods )
    {
        this.rewindRelativePeriods = rewindRelativePeriods;
    }

    // -------------------------------------------------------------------------
    // Merge with
    // -------------------------------------------------------------------------

    @Override
    public void mergeWith( IdentifiableObject other )
    {
        super.mergeWith( other );

        if ( other.getClass().isInstance( this ) )
        {
            Chart chart = (Chart) other;

            series = chart.getSeries();
            category = chart.getCategory();
            rewindRelativePeriods = chart.isRewindRelativePeriods();
        }
    }
}
