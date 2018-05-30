<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:tei="http://www.tei-c.org/ns/1.0" 
    exclude-result-prefixes="xs"
    version="2.0">
    <xsl:template match="/">
        <div id="transcript" class="transcript">
            <xsl:apply-templates select="//tei:body/*"/>
        </div>
    </xsl:template>
    
    <xsl:template match="tei:annotationBlock">
        <xsl:variable name="SPEAKER_ID" select="substring-after(@who,'#')"/>
        <div class="anotationBlock">
            <xsl:attribute name="id" select="@xml:id"/>
            <span class="speaker"><xsl:value-of select="//tei:person[@xml:id=$SPEAKER_ID]/@n"/></span>
            <xsl:text>: </xsl:text>
            <xsl:apply-templates select="tei:u/descendant::*"/>
        </div>
    </xsl:template>
    
    <xsl:template match="tei:w">
        <span class="token">
            <xsl:attribute name="id" select="@xml:id"/>
            <xsl:value-of select="."/><xsl:text> </xsl:text>
        </span>
    </xsl:template>
    
    <xsl:template match="tei:pause">
        <span class="pause">
            <xsl:attribute name="id" select="@xml:id"/>
            <xsl:choose>
                <xsl:when test="@type='micro'">(.) </xsl:when>
                <xsl:otherwise><xsl:text>(</xsl:text><xsl:value-of select="substring-before(substring-after(@dur, 'PT'), 'S')"/><xsl:text>) </xsl:text></xsl:otherwise>
            </xsl:choose>
        </span>
    </xsl:template>
    
    <xsl:template match="tei:desc">
        <span class="desc">
            <xsl:text>((</xsl:text>
            <xsl:value-of select="."/>
            <xsl:text>)) </xsl:text>
        </span>
    </xsl:template>

</xsl:stylesheet>