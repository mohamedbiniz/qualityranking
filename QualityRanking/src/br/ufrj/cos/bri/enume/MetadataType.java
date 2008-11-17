/**
 * 
 */
package br.ufrj.cos.bri.enume;

/**
 * @author Fabricio
 * 
 */
public enum MetadataType {
	/*
	 * Dublin Core From Wikipedia, the free encyclopedia
	 * 
	 * 
	 * The Dublin Core metadata element set is a standard for cross-domain
	 * information resource description. It provides a simple and standardised
	 * set of conventions for describing things online in ways that make them
	 * easier to find. Dublin Core is widely used to describe digital materials
	 * such as video, sound, image, text, and composite media like web pages.
	 * Implementations of Dublin Core typically make use of XML and are Resource
	 * Description Framework based. Dublin Core is defined by ISO in 2003 ISO
	 * Standard 15836, and NISO Standard Z39.85-2007.
	 * 
	 * The Dublin Core standard includes two levels: Simple and Qualified.
	 * Simple Dublin Core comprises fifteen elements; Qualified Dublin Core
	 * includes three additional elements (Audience, Provenance and
	 * RightsHolder), as well as a group of element refinements (also called
	 * qualifiers) that refine the semantics of the elements in ways that may be
	 * useful in resource discovery.
	 * 
	 * Simple Dublin Core
	 * 
	 * The Simple Dublin Core Metadata Element Set (DCMES) consists of 15
	 * metadata elements:
	 */
	// 1. Title
	TITLE,
	// 2. Creator
	CREATOR,
	// 3. Subject
	SUBJECT,
	// 4. Description
	DESCRIPTION,
	// 5. Publisher
	PUBLISHER,
	// 6. Contributor
	CONTRIBUTOR,
	// 7. Date
	DATE,
	// 8. Type
	TYPE,
	// 9. Format
	FORMAT,
	// 10. Identifier
	IDENTIFIER,
	// 11. Source
	SOURCE,
	// 12. Language
	LANGUAGE,
	// 13. Relation
	RELATION,
	// 14. Coverage
	COVERAGE,
	// 15. Rights
	RIGHTS,

	ENCODED;
}
