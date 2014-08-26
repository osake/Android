package com.eftimoff;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.eftimoff.OfyService.ofy;

@Api(name = "question", version = "v1", namespace = @ApiNamespace(ownerDomain = "eftimoff.com", ownerName = "eftimoff.com", packagePath = ""))
public class QuestionEndpoint {

	private static final Logger LOG = Logger.getLogger(QuestionEndpoint.class.getName());

	@ApiMethod(name = "insertQuestion")
	public void insertQuestion(@Named("id") String id) {
		if (findQuestion(id) != null) {
			LOG.info("Question " + id + " already registered, skipping register.");
			return;
		}
		final Question question = new Question();
		question.setId(id);
		ofy().save().entity(question).now();
	}

	@ApiMethod(name = "getQuestion")
	public Question getQuestion(@Named("id") String id) {
		Question question = findQuestion(id);
		if (question == null) {
			LOG.info("Device " + id + " not registered, skipping unregister");
		}
		return question;
	}

	@ApiMethod(name = "listQuestions")
	public CollectionResponse<Question> listQuestions(@Named("count") int count) {
		final List<Question> records = ofy().load().type(Question.class).limit(count).list();
		return CollectionResponse.<Question>builder().setItems(records).build();
	}

	private Question findQuestion(final String id) {
		return ofy().load().type(Question.class).filter("id", id).first().now();
	}
}