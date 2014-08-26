package com.eftimoff;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.eftimoff.OfyService.ofy;

@Api(name = "question", version = "v0.0.2", namespace = @ApiNamespace(ownerDomain = "eftimoff.com", ownerName = "eftimoff.com", packagePath = ""))
public class QuestionEndpoint {

	private static final Logger LOG = Logger.getLogger(QuestionEndpoint.class.getName());

	@ApiMethod(name = "insertQuestion", httpMethod = "post")
	public void insertQuestion(QuestionRecord question) {
		ofy().save().entity(question).now();
	}

	@ApiMethod(name = "getQuestion", httpMethod = "get")
	public QuestionRecord getQuestion(@Named("id") String id) {
		QuestionRecord question = findQuestion(id);
		if (question == null) {
			LOG.info("Device " + id + " not registered, skipping unregister");
		}
		return question;
	}

	@ApiMethod(name = "listQuestions", path = "list", httpMethod = "get")
	public List<QuestionRecord> listQuestions(@Named("count") int count) {
		return ofy().load().type(QuestionRecord.class).limit(count).list();
	}

	private QuestionRecord findQuestion(final String id) {
		return ofy().load().type(QuestionRecord.class).filter("id", id).first().now();
	}
}