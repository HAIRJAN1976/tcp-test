package harijan.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.flatbuffers.FlatBufferBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import network.model.Monster;

public class Client extends AbstractVerticle {

	private static Logger logger = LoggerFactory.getLogger(Client.class);

	@Override
	public void start() throws Exception {

		logger.debug("Start WebSocekt Client");

		HttpClient httpClient = vertx.createHttpClient();

		httpClient.websocket(1000, "172.30.13.143", "/test-uri", new Handler<WebSocket>() {

			@Override
			public void handle(WebSocket socket) {

				// crete flatbuffer data
				FlatBufferBuilder fbb = new FlatBufferBuilder(1);

				int str = fbb.createString("MyMonster");

				Monster.startMonster(fbb);
				Monster.addName(fbb, str);
				Monster.addHp(fbb, (short) 1);
				int mon = Monster.endMonster(fbb);
				fbb.finish(mon);

				byte[] monsterData = fbb.sizedByteArray();

				// send data to websocket
				socket.write(Buffer.buffer(monsterData));

				// processing receive data
				socket.handler(new Handler<Buffer>() {
					
					@Override
					public void handle(Buffer buffer) {
						logger.debug("recive data : {}", buffer.length());
						String receivedStr = buffer.getString(0, buffer.length());
						logger.debug(receivedStr);
					}
				});
			}
		});

		// netClient.connect(1000, "172.30.13.143", new
		// Handler<AsyncResult<NetSocket>>() {
		//
		// @Override
		// public void handle(AsyncResult<NetSocket> result) {
		// NetSocket netSocket = result.result();
		//
		// //netSocket.write("GET / HTTP/1.1\r\nHost: test.com\r\n\r\n");
		//
		// FlatBufferBuilder fbb = new FlatBufferBuilder(1);
		//
		// int str = fbb.createString("MyMonster");
		//
		// Monster.startMonster(fbb);
		// Monster.addName(fbb, str);
		// Monster.addHp(fbb, (short)1);
		// int mon = Monster.endMonster(fbb);
		// fbb.finish(mon);
		//
		// byte[] monsterData = fbb.sizedByteArray();
		//
		// netSocket.write(Buffer.buffer(monsterData));
		//
		// netSocket.handler(new Handler<Buffer>(){
		//
		// @Override
		// public void handle(Buffer buffer) {
		// logger.debug("recive data : {}", buffer.length());
		// String receivedStr = buffer.getString(0, buffer.length());
		//
		// logger.debug(receivedStr);
		// }
		//
		// });
		// }
		//
		// });

		super.start();
	}

	@Override
	public void stop() throws Exception {

		logger.debug("Stop Client");

		super.stop();
	}

}