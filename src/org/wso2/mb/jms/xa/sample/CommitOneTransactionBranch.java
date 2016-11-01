package org.wso2.mb.jms.xa.sample;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.XAConnection;
import javax.jms.XAConnectionFactory;
import javax.jms.XASession;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

/**
 * This example uses the two-phase commit protocol to commit one transaction branch
 */
public class CommitOneTransactionBranch {
    public static void main(String[] args) throws NamingException, JMSException, XAException {
        InitialContext initialContext = JMSClientHelper
                .getInitialContextForQueue("admin", "admin", "localhost", "5672", "xaTestQueue");

        XAConnectionFactory connectionFactory = (XAConnectionFactory) initialContext.lookup(JMSClientHelper
                .QUEUE_CONNECTION_FACTORY);

        XAConnection xaConnection = connectionFactory.createXAConnection();
        XASession xaSession = xaConnection.createXASession();

        XAResource xaResource = xaSession.getXAResource();
        Session session = xaSession.getSession();

        Destination xaTestQueue = (Destination) initialContext.lookup("xaTestQueue");
        MessageProducer producer = session.createProducer(xaTestQueue);

        MyXid xid = new MyXid(100, new byte[] { 0x01 }, new byte[] { 0x02 });

        xaResource.start(xid, XAResource.TMNOFLAGS);
        producer.send(session.createTextMessage("Test 1"));
        xaResource.end(xid, XAResource.TMSUCCESS);

        int ret = xaResource.prepare(xid);

        if (ret == XAResource.XA_OK) {
            xaResource.commit(xid, false);
        }

        session.close();
        xaConnection.close();
    }
}
