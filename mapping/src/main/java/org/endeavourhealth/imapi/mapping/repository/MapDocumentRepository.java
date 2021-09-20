package org.endeavourhealth.imapi.mapping.repository;

import org.endeavourhealth.imapi.dataaccess.ConnectionPool;
import org.endeavourhealth.imapi.dataaccess.repository.BaseRepository;
import org.endeavourhealth.imapi.mapping.model.MapDocument;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.StringJoiner;

public class MapDocumentRepository extends BaseRepository {

    public MapDocument findMapDocumentById(int id) throws SQLException {
        MapDocument mapDocument = new MapDocument();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.filename, c.document")
                .add("FROM map_document c")
                .add("WHERE c.dbid = ?");

        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                statement.setInt(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        mapDocument.setDocument(Base64.getDecoder().decode(rs.getString("document"))).setFilename(rs.getString("filename"));
                    }
                }
            }
        }

        return mapDocument;

    }

    public List<MapDocument> findAllMapDocuments() throws SQLException {
        List<MapDocument> mapDocuments = new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n")
                .add("SELECT c.dbid, c.filename")
                .add("FROM map_document c");

        try (Connection conn = ConnectionPool.get()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql.toString())) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        MapDocument mapDocument = new MapDocument();
                        mapDocument.setDbid(rs.getInt("dbid")).setFilename(rs.getString("filename"));
                        mapDocuments.add(mapDocument);
                    }
                }
            }
        }

        return mapDocuments;

    }
}