SELECT d.id-170, d.url, qd.name, dqd.score, m.id, m.`value`
FROM document as d, document_quality_dimension as dqd,quality_dimension qd, metadata as m
where d.id=dqd.document_id
      and dqd.quality_dimension_id=qd.id
     and m.document_id=d.id and qd.code<>'TIM'
order by qd.name asc, dqd.score desc;


SELECT d.id, d.url, qd.name, dqd.score, d.score
FROM document as d, document_quality_dimension as dqd,quality_dimension qd
where d.id=dqd.document_id
      and dqd.quality_dimension_id=qd.id
order by d.score desc, qd.name asc, dqd.score desc, d.id asc;

SELECT * FROM document_quality_dimension d;
SELECT * FROM quality_dimension q;
SELECT d.url, qd.name, m.id, m.`value`
FROM document as d, quality_dimension qd, metadata as m
where m.type='DATE' and m.document_id=d.id
   and qd.code='TIM';

SELECT * FROM quality_dimension_weight q;
SELECT * FROM quality_dimension q;
SELECt distinct (document_id-1169) FROM metadata;
SELECT * FROM metadata m;
SELECT * FROM outputlink_crawler as oc, page_crawler as pc
where oc.idpagina=pc.page_id;
SELECT * FROM outputlink_crawler oc
where oc.idpagina =0;
SELECT * FROM page_crawler p1;
SELECT * FROM page_crawler p1, page_crawler p2
where p1.id=p2.page_id;
SELECT * FROM dataset d;
SELECT * FROM document where document_id is null;
SELECT * FROM document order by score desc;
SELECT * FROM outputlink_crawler o;
DELETE FROM outputlink_crawler;
DELETE FROM metadata_crawler;
DELETE FROM page_crawler;
SELECT count(*) FROM page_crawler p;
SELECT * FROM page_crawler p order by ordem_download desc;
SELECT * FROM outputlink_crawler o where o.visited=0 order by date_create asc;

SELECT * FROM dataset_seed_documents d;

truncate document;
truncate Context_Quality_Dimension_Weight;
truncate Quality_Dimension_Weight;
truncate Quality_Dimension;
truncate Document_Quality_Dimension;
truncate Assessment_Scale;
truncate DataSet_Seed_Documents;
truncate DataSet_Collaborator;
truncate DataSet;
truncate Language;
truncate Collaborator;