<?php
$phpself = $_SERVER['PHP_SELF'];

function connect() {
	global $database, $conn;
	$conn = mysql_pconnect("{$database['host']}:{$database['port']}", $database['username'], $database['password']);
	mysql_select_db($database['catalog'], $conn);
	return $conn;
}

function disconnect() {
	global $conn;
	if (isset($conn)) {
		mysql_close($conn);
	}
}

function execute($sql) {
	global $conn;
	return mysql_query($sql, $conn);
}

function lastID() {
	global $conn;
	return mysql_insert_id($conn);
}

function query($sql) {
	global $conn;
	$result = mysql_query($sql, $conn);
	if ($result === FALSE) return FALSE;
	$rows = array();
	while ($row = mysql_fetch_assoc($result)) {
		foreach ($row as $key => $value) {
			$row[$key] = trim($value);
		}
		$rows[] = $row;
	}
	mysql_free_result($result);
	return $rows;
}

function first($sql) {
	$rows = query($sql);
	if (empty($rows)) return FALSE;
	return $rows[0];
}

function clean($str) {
	global $conn;
	return mysql_real_escape_string($str, $conn);
}

function success() {
	global $title, $header, $body;
	html($title, $header, $body);
	disconnect();
	exit(0);
}

function error($msg) {
	global $title, $header;
	html($title, $header, "<h2>Erro</h2>{$msg}");
	disconnect();
	exit(1);
}

function html($title, $header, $body) {
?><html>
<head><title><?php echo $title; ?></title></head>
<body><h1><?php echo $header; ?></h1><?php echo $body ?></body>
</html><?php
}

function elementToArray($element) {
	$array = array();
	foreach($element->attributes() as $attr => $value) {
		$array[$attr] = utf8_decode($value);
	}
	return $array;
}

function elementToInsert($element, $attributes = array()) {
	$attrs = array();
	$values = array();
	foreach($attributes as $attr => $value) {
		$attrs[] = $attr;
		$values[] = clean($value);
	}
	foreach($element->attributes() as $attr => $value) {
		$attrs[] = $attr;
		$values[] = clean(utf8_decode($value));
	}
	return "INSERT INTO `" . $element->getName() . "` (`" . implode("`, `", $attrs) . "`) VALUES ('" . implode("', '", $values) . "')";
}

function elementToDB($element, $attributes = array()) {
	$sql = elementToInsert($element, $attributes);
	$result = execute($sql);
	if (!$result) {
		error("Falha ao inserir no banco de dados.<p>{$sql}</p>");
	}
}

function initialize() {
	global $database, $admin;
	$ireval = simplexml_load_file('ireval.xml');
	if (!$ireval) {
		error('Não foi possível ler o arquivo de configuração (ireval.xml).');
	}
	$database = elementToArray($ireval->database[0]);
	$admin = elementToArray($ireval->admin[0]);
	if (!connect()) {
		error('Não foi possível conectar ao banco de dados.');
	}
}

function checkAdmin() {
	global $phpself;
	session_start();
	if (!isset($_SESSION['admin'])) {
		header("Location: {$phpself}");
		exit(0);
	}
}

function getExperiment() {
	global $title, $header;
	$title = $header = 'IREval';
	checkAdmin();
	initialize();
	if (!isset($_GET['id'])) {
		error('Nenhum experimento especificado.');
	}
	$experiment_id = clean($_GET['id']);
	$experiment = first("SELECT * FROM experiment WHERE id = {$experiment_id}");
	if (!$experiment) {
		error('Experimento inexistente.');
	}
	return $experiment;
}

function redirectToExperiment($id) {
	global $phpself;
	header("Location: $phpself?action=experiment&id={$id}");
	exit(0);
}

if (!isset($_GET['action'])) {
	$title = $header = 'IREval';
	$body = <<<BODY
		<form action="{$phpself}?action=admin" method="post">
		Senha: <input type="password" name="password" /> <input type="submit" value="Entrar" />
		</form>
BODY;
	success();
}

$action = $_GET['action'];

if ($action == 'admin') {
	$title = $header = 'IREval';
	initialize();
	session_start();
	if (!isset($_SESSION['admin'])) {
		if ($_POST['password'] != $admin['password']) {
			error('Senha incorreta.');
		}
		$_SESSION['admin'] = 1;
	}
	$body = <<<BODY
		<a href="{$phpself}?action=experiments">Experimentos</a> | <a href="{$phpself}?action=generate_experiment">Gerar</a>
BODY;
	success();
} else if ($action == 'experiments') {
	$title = 'IREval';
	$header = 'Experimentos';
	checkAdmin();
	initialize();
	$experiments = query('SELECT * FROM experiment ORDER BY id');
	$body = '';
	foreach ($experiments as $experiment) {
		$body .= <<<BODY
			<p><a href="{$phpself}?action=experiment&id={$experiment['id']}">Experimento {$experiment['id']} - {$experiment['subject']}</a></p>
BODY;
	}
	$body .= '<p><a href="' . $phpself . '?action=admin">Voltar</a></p>';
	success();
} else if ($action == 'open_experiment') {
	$experiment = getExperiment();
	$result = execute("UPDATE experiment SET status = 'O' WHERE id = {$experiment['id']}");
	if (!$result) {
		error('Falha ao abrir experimento.');
	}
	redirectToExperiment($experiment['id']);
} else if ($action == 'close_experiment') {
	$experiment = getExperiment();
	$result = execute("UPDATE experiment SET status = 'C' WHERE id = {$experiment['id']}");
	if (!$result) {
		error('Falha ao fechar experimento.');
	}
	redirectToExperiment($experiment['id']);
} else if ($action == 'experiment') {
	$experiment = getExperiment();
	$header = "Experimento {$experiment['id']} - {$experiment['subject']}";
	if ($experiment['status'] == 'C') {
		$status = 'fechada';
		$status_action = 'open';
		$status_link = 'Abrir';
	} else if ($experiment['status'] == 'O') {
		$status = 'aberta';
		$status_action = 'close';
		$status_link = 'Fechar';
	}
	$url = "http://{$_SERVER['HTTP_HOST']}{$phpself}?action=evaluate&id={$experiment['id']}";
	$body = <<<BODY
	Avaliação: <strong>{$status}</strong> <a href="{$phpself}?action={$status_action}_experiment&id={$experiment['id']}">{$status_link}</a><br />
	URL para avaliadores: <a href="{$url}" target="_blank">{$url}</a>
BODY;
	$evaluators = query("
		SELECT e.email, ee.start_datetime, ee.end_datetime FROM evaluator AS e
		INNER JOIN experiment_evaluation AS ee ON ee.experiment_id = e.experiment_id AND ee.evaluator_id = e.id
		WHERE e.experiment_id = {$experiment['id']}
		ORDER BY e.email");
	if (empty($evaluators)) {
		error('Falha ao carregar avaliadores.');
	}
	$body .= '<p><table border="1"><thead><tr><td>Avaliador</td><td>Início</td><td>Fim</td></tr></thead><tbody>';
	foreach ($evaluators as $evaluator) {
		$body .= "<tr><td>{$evaluator['email']}</td><td>{$evaluator['start_datetime']}</td><td>{$evaluator['end_datetime']}</td></tr>";
	}
	$body .= '</tbody></table></p>';
	$body .= '<p><a href="' . $phpself . '?action=admin">Voltar</a></p>';
	success();
} else if ($action == 'generate_experiment') {
	$title = 'IREval';
	$header = 'Gerar experimento';
	checkAdmin();
	initialize();
	if (!isset($_POST['descriptor_xml']) && !isset($_FILES['descriptor_xml_file'])) {
		$xml = file_get_contents('experiment.xml');
		$body = <<<BODY
			<form enctype="multipart/form-data" action="{$phpself}?action={$action}" method="post">
			Descritor de experimento em XML:
			<p><textarea name="descriptor_xml" cols="80" rows="15">{$xml}</textarea></p>
			<p>Ou arquivo do descritor de experimento em XML: <input type="file" name="descriptor_xml_file" /></p>
			<input type="submit" value="Gerar" />
			</form>
BODY;
		success();
	}
	if (isset($_FILES['descriptor_xml_file']) && is_uploaded_file($_FILES['descriptor_xml_file']['tmp_name'])) {
        if (!move_uploaded_file($_FILES['descriptor_xml_file']['tmp_name'], 'temp.xml')) {
			error('Falha ao mover arquivo enviado.');
		}
		$experiment = simplexml_load_file('temp.xml');
	} else 	if (isset($_POST['descriptor_xml'])) {
		$experiment = simplexml_load_string($_POST['descriptor_xml']);
	}
	if (!$experiment) {
		error('Descritor de experimento inválido.');
	}
	elementToDB($experiment);
	$experiment_id = lastID();
	if (file_exists('temp.xml')) {
		rename('temp.xml', "experiment{$experiment_id}.xml");
	}
	$attributes = array('experiment_id' => $experiment_id);
	foreach ($experiment->children() as $child) {
		elementToDB($child, $attributes);
	}
	$experiment = first("SELECT * FROM experiment WHERE id = {$experiment_id}");
	if ($experiment === FALSE) {
		error('Falha ao carregar experimento do banco de dados.');
	}
	$evaluators = query("SELECT id FROM evaluator WHERE experiment_id = {$experiment_id}");
	if (empty($evaluators)) {
		error('Não há avaliadores cadastrados no banco de dados.');
	}
	foreach ($evaluators as $evaluator) {
		execute("
			INSERT INTO experiment_evaluation (experiment_id, evaluator_id)
			VALUES ({$experiment_id}, {$evaluator['id']})");
	}
	$docs = query("SELECT * FROM document WHERE experiment_id = {$experiment_id}");
	if (empty($docs)) {
		error('Não há documentos cadastrados no banco de dados.');
	}
	foreach ($docs as $doc) {
		$ids = '0';
		for ($i = 0; $i < $experiment['evaluations_per_document']; ++$i) {
			$evaluators = query("
				SELECT e.id
				FROM evaluator AS e
				LEFT OUTER JOIN document_evaluation AS de ON de.evaluator_id = e.id
				LEFT OUTER JOIN document AS d ON d.id = de.document_id
				WHERE e.experiment_id = {$experiment_id} AND e.id NOT IN ({$ids})
				GROUP BY e.id
				HAVING COUNT(DISTINCT de.document_id) < {$experiment['documents_per_evaluator']}");
			if (empty($evaluators)) {
				break 2;
			}
			$evaluator = $evaluators[array_rand($evaluators)];
			$ids .= ", {$evaluator['id']}";
			$result = execute("
				INSERT INTO document_evaluation (document_id, evaluator_id, linguistic_variable_id)
				SELECT {$doc['id']}, {$evaluator['id']}, id
				FROM linguistic_variable
				WHERE experiment_id = {$experiment_id}");
			if (!$result) {
				error('Falha ao associar avaliação de documento a avaliador.');
			}
			$result = execute("
				INSERT INTO document_relevancy (document_id, evaluator_id)
				VALUES ({$doc['id']}, {$evaluator['id']})");
			if (!$result) {
				error('Falha ao associar relevância de documento a avaliador.');
			}
			$result = execute("
				INSERT INTO query_evaluation (document_id, query_id, evaluator_id)
				SELECT {$doc['id']}, id, {$evaluator['id']}
				FROM query
				WHERE experiment_id = {$experiment_id}");
			if (!$result) {
				error('Falha ao associar pergunta a avaliador.');
			}
		}
	}
	redirectToExperiment($experiment_id);
} else if ($action == 'evaluate') {
	$title = 'IREval';
	$header = 'Avaliação de páginas';
	initialize();
	if (!isset($_POST['email'])) {
		if (!isset($_GET['id'])) {
			error('Nenhum experimento especificado.');
		}
		$experiment_id = clean($_GET['id']);
		$body = <<<BODY
		<form action="{$phpself}?action={$action}" method="post">
		<input type="hidden" name="experiment_id" value="{$experiment_id}" />
		Seu e-mail: <input type="text" name="email" /> <input type="submit" value="Entrar" />
		</form>
BODY;
		success();
	}
	if (!isset($_POST['experiment_id'])) {
		error('Nenhum experimento especificado.');
	}
	$email = clean($_POST['email']);
	$experiment_id = clean($_POST['experiment_id']);
	$experiment = first("SELECT * FROM experiment WHERE id = {$experiment_id}");
	if ($experiment === FALSE) {
		error('Experimento inexistente. Por favor, entre em contato com o pesquisador responsável.');
	}
	$evaluator = first("SELECT * FROM evaluator WHERE experiment_id = {$experiment_id} AND email = '{$email}'");
	if ($evaluator === FALSE) {
		error("Você não está cadastrado como avaliador para o experimento. Por favor, entre em contato com o pesquisador responsável no e-mail {$experiment['researcher_email']}.");
	}
	if ($experiment['status'] == 'C') {
		error('Experimento fechado.');
	}
	$title = $header = "Avaliação de páginas - {$experiment['subject']}";
	if (isset($_POST['docs'])) {
		if (!isset($_POST['name']) || !isset($_POST['education_option_id']) || !isset($_POST['activity_id'])) {
			error("Falha ao salvar dados do avaliador. Por favor, entre em contato com o pesquisador responsável no e-mail {$experiment['researcher_email']}.");
		}
		$name = clean($_POST['name']);
		$education_option_id = clean($_POST['education_option_id']);
		if ($education_option_id == '') $education_option_id = 'NULL';
		$activity_id = clean($_POST['activity_id']);
		if ($activity_id == '') $activity_id = 'NULL';		
		$result = execute("
			UPDATE evaluator
			SET name = '{$name}', education_option_id = {$education_option_id}, activity_id = {$activity_id}
			WHERE experiment_id = {$experiment_id} AND id = {$evaluator['id']}");
		if (!$result) {
			error("Falha ao salvar dados do avaliador. Por favor, entre em contato com o pesquisador responsável no e-mail {$experiment['researcher_email']}.");
		}
		if (!isset($_POST['knowledge_rating_id'])) {
			error("Falha ao salvar auto-avaliação. Por favor, entre em contato com o pesquisador responsável no e-mail {$experiment['researcher_email']}.");
		}
		$knowledge_rating_id = clean($_POST['knowledge_rating_id']);
		if ($knowledge_rating_id == '') $knowledge_rating_id = 'NULL';
		$result = execute("
			UPDATE experiment_evaluation
			SET knowledge_rating_id = {$knowledge_rating_id}
			WHERE experiment_id = {$experiment_id} AND evaluator_id = {$evaluator['id']}");
		if (!$result) {
			error("Falha ao salvar auto-avaliação. Por favor, entre em contato com o pesquisador responsável no e-mail {$experiment['researcher_email']}.");
		}
		foreach ($_POST['docs'] as $doc_id => $doc) {
			foreach ($doc['queries'] as $query_id => $answer) {
				$query_id = clean($query_id);
				$answer = clean($answer);
				if ($answer == '') $answer = 'NULL';
				$result = execute("
					UPDATE query_evaluation
					SET relevant = {$answer}
					WHERE document_id = {$doc_id} AND query_id = {$query_id} AND evaluator_id = {$evaluator['id']}");
				if (!$result) {
					error("Falha ao salvar avaliação. Por favor, entre em contato com o pesquisador responsável no e-mail {$experiment['researcher_email']}.");
				}
			}
			foreach ($doc['linguistic_variables'] as $linguistic_variable_id => $linguistic_term_id) {
				$linguistic_variable_id = clean($linguistic_variable_id);
				$linguistic_term_id = clean($linguistic_term_id);
				if ($linguistic_term_id == '') $linguistic_term_id = 'NULL';
				$result = execute("
					UPDATE document_evaluation
					SET linguistic_term_id = {$linguistic_term_id}
					WHERE document_id = {$doc_id} AND evaluator_id = {$evaluator['id']} AND linguistic_variable_id = {$linguistic_variable_id}");
				if (!$result) {
					error("Falha ao salvar avaliação. Por favor, entre em contato com o pesquisador responsável no e-mail {$experiment['researcher_email']}.");
				}
			}
			$relevant = clean($doc['relevant']);
			if ($relevant == '') $relevant = 'NULL';
			$result = execute("
					UPDATE document_relevancy
					SET relevant = {$relevant}
					WHERE document_id = {$doc_id} AND evaluator_id = {$evaluator['id']}");
			if (!$result) {
				error("Falha ao salvar avaliação. Por favor, entre em contato com o pesquisador responsável no e-mail {$experiment['researcher_email']}.");
			}
		}
		$nulls = query("
			SELECT 1 FROM evaluator
			WHERE experiment_id = {$experiment_id} AND id = {$evaluator['id']}
			AND (name IS NULL OR education_option_id IS NULL OR activity_id IS NULL)
			UNION
			SELECT 1 FROM experiment_evaluation
			WHERE experiment_id = {$experiment_id} AND evaluator_id = {$evaluator['id']} AND knowledge_rating_id IS NULL
			UNION
			SELECT 1 FROM document_evaluation AS de
			INNER JOIN document AS d ON d.id = de.document_id
			WHERE d.experiment_id = {$experiment_id} AND de.evaluator_id = {$evaluator['id']} AND de.linguistic_term_id IS NULL
			UNION
			SELECT 1 FROM document_relevancy AS dr
			INNER JOIN document AS d ON d.id = dr.document_id
			WHERE d.experiment_id = {$experiment_id} AND dr.evaluator_id = {$evaluator['id']} AND dr.relevant IS NULL
			UNION
			SELECT 1 FROM query_evaluation AS qe
			INNER JOIN document AS d ON d.id = qe.document_id
			WHERE d.experiment_id = {$experiment_id} AND qe.evaluator_id = {$evaluator['id']} AND qe.relevant IS NULL");
		if (count($nulls) > 0) {
			$body = <<<BODY
				Atenção: <strong>alguns itens ficaram sem resposta</strong>.<p>Você pode voltar e completar a avaliação agora ou pode fazê-lo posteriormente.</p>
				<form action="{$phpself}?action={$action}" method="post">
				<input type="hidden" name="experiment_id" value="{$experiment_id}" />
				<input type="hidden" name="email" value="{$email}" />
				<input type="submit" value="Voltar" />
				</form>
BODY;
		} else {
			execute("
				UPDATE experiment_evaluation
				SET end_datetime = NOW()
				WHERE experiment_id = {$experiment_id} AND evaluator_id = {$evaluator['id']} AND end_datetime IS NULL");
			$body = "Muito obrigado por sua participação, <strong>{$email}</strong>! Você completou a avaliação com sucesso!";
		}
		success();
	}

	// Evaluator
	$education_options = query("SELECT * FROM education_option WHERE experiment_id = {$experiment_id} ORDER BY id");
	$activities = query("SELECT * FROM activity WHERE experiment_id = {$experiment_id} ORDER BY id");
	if ($evaluator === FALSE|| empty($education_options) || empty($activities)) {
		error("Falha ao carregar dados do avaliador. Por favor, entre em contato com o pesquisador responsável no e-mail {$experiment['researcher_email']}.");
	}
	array_unshift($education_options, array('id' => '', 'name' => 'Sem resposta'));
	array_unshift($activities, array('id' => '', 'name' => 'Sem resposta'));
	$evaluator_html = '<h2>Sobre você</h2><p>Nome: <input type="text" name="name" value="' . $evaluator['name'] . '" /></p>';
	$evaluator_html .= '<p>Escolaridade: <select name="education_option_id">';
	foreach ($education_options as $education_option) {
		$selected = $evaluator['education_option_id'] === $education_option['id'] ? ' selected="selected"' : '';
		$evaluator_html .= '<option value="' . $education_option['id'] . '"' . $selected . '>' . $education_option['name'] . '</option>';
	}
	$evaluator_html .= '</select></p>';
	$evaluator_html .= '<p>Atividade: <select name="activity_id">';
	foreach ($activities as $activity) {
		$selected = $evaluator['activity_id'] === $activity['id'] ? ' selected="selected"' : '';
		$evaluator_html .= '<option value="' . $activity['id'] . '"' . $selected . '>' . $activity['name'] . '</option>';
	}
	$evaluator_html .= '</select></p>';	
	// Experiment evaluation
	$experiment_evaluation = first("
		SELECT * FROM experiment_evaluation
		WHERE experiment_id = {$experiment_id} AND evaluator_id = {$evaluator['id']}");
	$ratings = query("SELECT * FROM knowledge_rating WHERE experiment_id = {$experiment_id} ORDER BY id");
	if ($experiment_evaluation === FALSE|| empty($ratings)) {
		error("Falha ao carregar auto-avaliação. Por favor, entre em contato com o pesquisador responsável no e-mail {$experiment['researcher_email']}.");
	}
	array_unshift($ratings, array('id' => '', 'name' => 'Sem resposta'));
	$ratings_html = '<h2>Auto-avaliação</h2>Qual o seu conhecimento sobre o assunto (<strong>' . $experiment['subject'] . '</strong>)? <select name="knowledge_rating_id">';
	foreach ($ratings as $rating) {
		$selected = $experiment_evaluation['knowledge_rating_id'] === $rating['id'] ? ' selected="selected"' : '';
		$ratings_html .= '<option value="' . $rating['id'] . '"' . $selected . '>' . $rating['name'] . '</option>';
	}
	$ratings_html .= '</select>';
	// Queries
	$docs = query("
		SELECT DISTINCT d.id, d.url, dr.relevant
		FROM document_evaluation AS de
		INNER JOIN document AS d ON d.id = de.document_id
		INNER JOIN document_relevancy AS dr ON dr.document_id = de.document_id AND dr.evaluator_id = de.evaluator_id
		WHERE d.experiment_id = {$experiment_id} AND de.evaluator_id = {$evaluator['id']}
		ORDER BY d.id");
	$doc_count = count($docs);
	$queries = query("SELECT * FROM `query` WHERE experiment_id = {$experiment_id} ORDER BY id");
	$query_count = count($queries);
	$linguistic_variables = query("SELECT * FROM linguistic_variable WHERE experiment_id = {$experiment_id} ORDER BY id");
	$linguistic_variable_count = count($linguistic_variables);
	$linguistic_terms = query("SELECT * FROM linguistic_term WHERE experiment_id = {$experiment_id} ORDER BY id");
	$linguistic_term_count = count($linguistic_terms);
	if (empty($docs) || empty($queries) || empty($linguistic_variables) || empty ($linguistic_terms)) {
		error("Falha ao carregar avaliação. Por favor, entre em contato com o pesquisador responsável no e-mail {$experiment['researcher_email']}.");
	}
	// Instructions
	$instructions_html = "<h2>Avaliação das páginas</h2>Você será responsável por avaliar <strong>{$doc_count} páginas</strong> no contexto de {$experiment['subject']}.
		Para <strong>cada página</strong> apresentada, você deve: <ol><li>Visitá-la clicando no link fornecido (você pode mantê-la aberta durante a avaliação e consultá-la sempre que preciso).</li>
		<p>Em seguida, você responderá às seguintes <strong>{$query_count} perguntas</strong>, escolhendo <strong>Sim</strong> ou <strong>Não</strong>:</p>";
	foreach ($queries as $query) {
		$instructions_html .= "<li>{$query['description']}</li>";
	}
	$linguistic_term_names = '';
	for ($i = 0; $i < $linguistic_term_count; ++$i) {
		if ($i == $linguistic_term_count - 1) {
			$linguistic_term_names .= ' ou ';
		} else if ($i > 0) {
			$linguistic_term_names .= ', ';
		}
		$linguistic_term_names .= "<strong>{$linguistic_terms[$i]['name']}</strong>";
	}
	$instructions_html .= "<p>Depois, você avaliará <strong>{$linguistic_variable_count} atributos</strong> de qualidade da página, qualificando cada atributo como {$linguistic_term_names}:</p>";
	foreach ($linguistic_variables as $linguistic_variable) {
		$instructions_html .= "<li>{$linguistic_variable['name']}</li><em>{$linguistic_variable['description']}</em>";
	}
	$instructions_html .= "<p>Por fim, você informará se a página é relevante para o assunto ({$experiment['subject']}).</p><p>Se você tiver quaisquer dúvidas, por favor, envie um e-mail para <strong>{$experiment['researcher_email']}</strong>.</p></ol>";
	
	// Query and document evaluation
	$answers = array('' => 'Sem resposta', '1' => 'Sim', '0' => 'Não');
	array_unshift($linguistic_terms, array('id' => '', 'name' => 'Sem resposta'));
	$docs_html = '';
	$i = 0;
	foreach ($docs as $doc) {
		++$i;
		$docs_html .= '<h3>Página ' . $i . '</h3><ol><li>Visite a página clicando <a href="' . $doc['url'] . '" target="_blank">aqui</a>.</li>';
		$docs_html .= '<p>Perguntas:</p>';
		$queries = query("
			SELECT q.id, q.description, qe.relevant
			FROM query_evaluation AS qe
			INNER JOIN query AS q ON q.id = qe.query_id
			WHERE qe.document_id = {$doc['id']} AND qe.evaluator_id = {$evaluator['id']}
			ORDER BY q.id");
		if (empty($queries)) {
			error("Falha ao carregar avaliação. Por favor, entre em contato com o pesquisador responsável no e-mail {$experiment['researcher_email']}.");
		}
		foreach ($queries as $query) {
			$docs_html .= '<li>' . $query['description'] . '<br /><select name="docs[' . $doc['id'] . '][queries][' . $query['id'] . ']">';
			foreach ($answers as $value => $answer) {
				$selected = strcmp($query['relevant'], $value) == 0 ? ' selected="selected"' : '';
				$docs_html .= '<option value="' . $value . '"' . $selected . '>' . $answer . '</option>';
			}
			$docs_html .= '</select></li>';
		}
		$docs_html .= '<p>Atributos de qualidade:</p>';
		$linguistic_variables = query("
			SELECT lv.id, lv.name, de.linguistic_term_id
			FROM document_evaluation AS de
			INNER JOIN linguistic_variable AS lv ON lv.id = de.linguistic_variable_id
			WHERE de.document_id = {$doc['id']} AND de.evaluator_id = {$evaluator['id']}
			ORDER BY lv.id");
		if (empty($linguistic_variables)) {
			error("Falha ao carregar avaliação. Por favor, entre em contato com o pesquisador responsável no e-mail {$experiment['researcher_email']}.");
		}
		foreach ($linguistic_variables as $linguistic_variable) {
			$docs_html .= '<li>' . $linguistic_variable['name'] . '<br /><select name="docs[' . $doc['id'] . '][linguistic_variables][' . $linguistic_variable['id'] . ']">';
			foreach ($linguistic_terms as $linguistic_term) {
				$selected = $linguistic_variable['linguistic_term_id'] === $linguistic_term['id'] ? ' selected="selected"' : '';
				$docs_html .= '<option value="' . $linguistic_term['id'] . '"' . $selected . '>' . $linguistic_term['name'] . '</option>';
			}
			$docs_html .= '</select></li>';
		}
		$docs_html .= '<p>A página é relevante para o assunto (' . $experiment['subject'] . ')? <select name="docs[' . $doc['id'] . '][relevant]">';
		foreach ($answers as $value => $answer) {
			$selected = strcmp($doc['relevant'], $value) == 0 ? ' selected="selected"' : '';
			$docs_html .= '<option value="' . $value . '"' . $selected . '>' . $answer . '</option>';
		}
		$docs_html .= '</select></p>';
		$docs_html .= '</ol>';
	}
	$body = <<<BODY
		<form action="{$phpself}?action={$action}" method="post">
		<input type="hidden" name="experiment_id" value="{$experiment_id}" />
		<input type="hidden" name="email" value="{$email}" />
		Bem-vindo, <strong>{$email}</strong>! Obrigado por aceitar participar da nossa pesquisa!
		{$evaluator_html}
		{$ratings_html}
		{$instructions_html}
		{$docs_html}
		<input type="submit" value="Salvar" />
		</form>
BODY;
	execute("
		UPDATE experiment_evaluation
		SET start_datetime = NOW()
		WHERE experiment_id = {$experiment_id} AND evaluator_id = {$evaluator['id']} AND start_datetime IS NULL");
	success();
} else {
	$title = $header = 'IREval';
	error('Ação inexistente.');
}
?>