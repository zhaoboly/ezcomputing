package org.ezcomputing.server.manager;

import java.util.HashMap;
import java.util.Map;

import org.ezcomputing.server.dao.dto.Constants;
import org.ezcomputing.server.dao.dto.Job;
import org.ezcomputing.server.dao.dto.Module;
import org.ezcomputing.server.util.ResourceUtils;
import org.ezcomputing.worker.domain.JobLogBean;
import org.ezcomputing.worker.graalvm.member.Http;
import org.ezcomputing.worker.graalvm.member.Test;
import org.ezcomputing.worker.manager.JsManager;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * @author Bo Zhao
 *
 */
@Component
public class JsServerManager extends JsManager {

	private static final Logger logger = LoggerFactory.getLogger(JsServerManager.class);

	@org.springframework.beans.factory.annotation.Value("${remote.function.host}")
	private String remoteFunctionHost;

	public void runJavascript(Module module) {

		long time = System.currentTimeMillis();
		String error = null;
		String status = null;

		try {
			Engine engine = Engine.create();

			Context context = Context.newBuilder().engine(engine).allowAllAccess(false).build();

			context.eval(Source.create("js", ResourceUtils.readResource("js/lang.js")));

			Map<String, String> modules = new HashMap<String, String>();
			loadModules(modules, module.getSource());

			// add js codes
			for (String code : modules.values()) {
				context.eval(Source.create("js", code));
			}

			// add build-in functions
			if (modules.containsKey("http")) {
				context.getBindings("js").putMember("http", new Http());
			}
			if (modules.containsKey("test")) {
				context.getBindings("js").putMember("test", new Test());
			}

			context.eval(Source.create("js", module.getSource()));
			context.eval("js", module.getNamespace() + "_main()");

			status = Constants.JOB_STATUS.success;

		} catch (Exception e) {
			// e.printStackTrace();
			error = e.getMessage();
			status = Constants.JOB_STATUS.failed;

		} finally {
			long duration = System.currentTimeMillis() - time;

			// DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			// job.addJobLog(workerName, formatter.format(new Date()),
			// String.valueOf(duration),
			// status, error);

			module.setStatus(status);
			module.setError(error);
		}

	}

	public void runJavascript(Job job, String workerName) {

		JobLogBean log = super.runJavascript(job.toJobBean(), workerName);

		job.setError(log.getError());
		job.setStatus(log.getStatus());
		job.setResponse(log.getResponse());

		job.addJobLog(workerName, log.getEndTime(), log.getDuration(), log.getStatus(), log.getError());

	}

}
