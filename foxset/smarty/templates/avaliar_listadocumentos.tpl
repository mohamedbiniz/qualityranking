<b>{$docs[0].dataset_context}</b><br>{$docs[0].dataset_description}<br><br>

<ul>
{foreach item=doc key=docID from=$docs}
	<li><a href="?idurl={$doc.document_id}">{$doc.document_url}</a></li>
{/foreach}
</ul>	
