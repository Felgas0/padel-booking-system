-- Insert users with valid UUID tokens and password hashes
INSERT INTO users(name, email, token, password_hash)
VALUES
    (
        'Luis Trindade',
        'A32334@alunos.isel.pt',
        '49698b60-12ca-4df7-8950-d783124f5fa0',
        '$2a$10$YnlzFXCghoHxL1QJXc8ehOGPgOJGvokNkW9ZQGiMSw98nJzDhH2qu' -- passwordLuis
    ),
    (
        'Antonio Pimentel',
        'A42146@alunos.isel.pt',
        '537b4555-161a-40fd-b9d6-290800b73dc8',
        '$2a$10$9O3mHwPyWsWk.WMx94sPbeTuYJjYvLCPBgTdXZqF77S4Z1zG9p4ty' -- passwordAntonio
    ),
    (
        'Martim Felgas',
        'A51838@alunos.isel.pt',
        '2ccc299a-db9a-414f-bcab-28ecb541146b',
        '$2a$10$VOeSbbqbkrWXAJx8RYyP5.xAnMslMyRw4n6Y0ZMIboZzGHAl9kgoa' -- passwordMartim
    );

-- Insert 25 clubs owned by users (cycled through 1, 2, 3)
INSERT INTO clubs(name, owner_id)
VALUES
    ('Club Name 1', 1), ('Club Name 2', 2), ('Club Name 3', 3), ('Club Name 4', 1),
    ('Club Name 5', 2), ('Club Name 6', 3), ('Club Name 7', 1), ('Club Name 8', 2),
    ('Club Name 9', 3), ('Club Name 10', 1), ('Club Name 11', 2), ('Club Name 12', 3),
    ('Club Name 13', 1), ('Club Name 14', 2), ('Club Name 15', 3), ('Club Name 16', 1),
    ('Club Name 17', 2), ('Club Name 18', 3), ('Club Name 19', 1), ('Club Name 20', 2),
    ('Club Name 21', 3), ('Club Name 22', 1), ('Club Name 23', 2), ('Club Name 24', 3),
    ('Club Name 25', 1);

-- Insert 25 courts (1 per club)
INSERT INTO courts(name, club_id)
VALUES
    ('Court 1', 1), ('Court 2', 2), ('Court 3', 3), ('Court 4', 4), ('Court 5', 5),
    ('Court 6', 6), ('Court 7', 7), ('Court 8', 8), ('Court 9', 9), ('Court 10', 10),
    ('Court 11', 11), ('Court 12', 12), ('Court 13', 13), ('Court 14', 14), ('Court 15', 15),
    ('Court 16', 16), ('Court 17', 17), ('Court 18', 18), ('Court 19', 19), ('Court 20', 20),
    ('Court 21', 21), ('Court 22', 22), ('Court 23', 23), ('Court 24', 24), ('Court 25', 25);

-- Insert 50 rentals (cycling through user_id and court_id)
INSERT INTO rentals(date, duration, user_id, court_id)
VALUES
    (TIMESTAMP '2025-08-01 10:00:00', 1, 1, 1),
    (TIMESTAMP '2025-08-01 11:00:00', 2, 2, 2),
    (TIMESTAMP '2025-08-01 12:00:00', 3, 3, 3),
    (TIMESTAMP '2025-08-01 13:00:00', 1, 1, 4),
    (TIMESTAMP '2025-08-01 14:00:00', 2, 2, 5),
    (TIMESTAMP '2025-08-01 15:00:00', 1, 3, 6),
    (TIMESTAMP '2025-08-01 16:00:00', 2, 1, 7),
    (TIMESTAMP '2025-08-01 17:00:00', 1, 2, 8),
    (TIMESTAMP '2025-08-01 18:00:00', 2, 3, 9),
    (TIMESTAMP '2025-08-01 19:00:00', 1, 1, 10),
    (TIMESTAMP '2025-08-02 10:00:00', 2, 2, 11),
    (TIMESTAMP '2025-08-02 11:00:00', 1, 3, 12),
    (TIMESTAMP '2025-08-02 12:00:00', 2, 1, 13),
    (TIMESTAMP '2025-08-02 13:00:00', 1, 2, 14),
    (TIMESTAMP '2025-08-02 14:00:00', 2, 3, 15),
    (TIMESTAMP '2025-08-02 15:00:00', 1, 1, 16),
    (TIMESTAMP '2025-08-02 16:00:00', 2, 2, 17),
    (TIMESTAMP '2025-08-02 17:00:00', 1, 3, 18),
    (TIMESTAMP '2025-08-02 18:00:00', 2, 1, 19),
    (TIMESTAMP '2025-08-02 19:00:00', 1, 2, 20),
    (TIMESTAMP '2025-08-03 10:00:00', 2, 3, 21),
    (TIMESTAMP '2025-08-03 11:00:00', 1, 1, 22),
    (TIMESTAMP '2025-08-03 12:00:00', 2, 2, 23),
    (TIMESTAMP '2025-08-03 13:00:00', 1, 3, 24),
    (TIMESTAMP '2025-08-03 14:00:00', 2, 1, 25),
    (TIMESTAMP '2025-08-03 15:00:00', 1, 2, 1),
    (TIMESTAMP '2025-08-03 16:00:00', 2, 3, 2),
    (TIMESTAMP '2025-08-03 17:00:00', 1, 1, 3),
    (TIMESTAMP '2025-08-03 18:00:00', 2, 2, 4),
    (TIMESTAMP '2025-08-03 19:00:00', 1, 3, 5),
    (TIMESTAMP '2025-08-04 10:00:00', 2, 1, 6),
    (TIMESTAMP '2025-08-04 11:00:00', 1, 2, 7),
    (TIMESTAMP '2025-08-04 12:00:00', 2, 3, 8),
    (TIMESTAMP '2025-08-04 13:00:00', 1, 1, 9),
    (TIMESTAMP '2025-08-04 14:00:00', 2, 2, 10),
    (TIMESTAMP '2025-08-04 15:00:00', 1, 3, 11),
    (TIMESTAMP '2025-08-04 16:00:00', 2, 1, 12),
    (TIMESTAMP '2025-08-04 17:00:00', 1, 2, 13),
    (TIMESTAMP '2025-08-04 18:00:00', 2, 3, 14),
    (TIMESTAMP '2025-08-04 19:00:00', 1, 1, 15),
    (TIMESTAMP '2025-08-05 10:00:00', 2, 2, 16),
    (TIMESTAMP '2025-08-05 11:00:00', 1, 3, 17),
    (TIMESTAMP '2025-08-05 12:00:00', 2, 1, 18),
    (TIMESTAMP '2025-08-05 13:00:00', 1, 2, 19),
    (TIMESTAMP '2025-08-05 14:00:00', 2, 3, 20),
    (TIMESTAMP '2025-08-05 15:00:00', 1, 1, 21),
    (TIMESTAMP '2025-08-05 16:00:00', 2, 2, 22),
    (TIMESTAMP '2025-08-05 17:00:00', 1, 3, 23),
    (TIMESTAMP '2025-08-05 18:00:00', 2, 1, 24),
    (TIMESTAMP '2025-08-05 19:00:00', 1, 2, 25);
