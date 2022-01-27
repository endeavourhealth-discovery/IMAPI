package org.endeavourhealth.imapi.filer.rdf4j;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.filer.TTDocumentFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TTDocumentFilerRdf4j extends TTDocumentFiler {
    private static final Logger LOG = LoggerFactory.getLogger(TTDocumentFilerRdf4j.class);

    private RepositoryConnection conn;

    public TTDocumentFilerRdf4j() {
        LOG.info("Connecting");
        conn = ConnectionManager.getConnection();

        LOG.info("Initializing");
        conceptFiler = new TTEntityFilerRdf4j(conn, prefixMap);
        instanceFiler = conceptFiler;   // Concepts & Instances filed in the same way
        LOG.info("Done");
    }

    @Override
    protected void startTransaction() throws TTFilerException {
        try {
            conn.begin();
        } catch (RepositoryException e) {
            throw new TTFilerException("Failed to start transaction", e);
        }
    }

    @Override
    protected void commit() throws TTFilerException {
        try {
            conn.commit();
        } catch (RepositoryException e) {
            throw new TTFilerException("Failed to commit transaction", e);
        }
    }

    @Override
    protected void rollback() throws TTFilerException {
        try {
            conn.rollback();
        } catch (RepositoryException e) {
            throw new TTFilerException("Failed to rollback transaction", e);
        }
    }

    @Override
    public void close() {
        LOG.info("Disconnecting");
        conn.close();
        LOG.info("Disconnected");
    }
}
