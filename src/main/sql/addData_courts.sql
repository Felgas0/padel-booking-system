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

-- Insert clubs owned by users
INSERT INTO clubs(name, owner_id)
VALUES
    ('Luis club', 1),
    ('Antonio club', 2),
    ('Martim club', 3);

-- Insert courts in each club
INSERT INTO courts(name, club_id)
VALUES
    ('Court A', 1),
    ('Court B', 2),
    ('Court C', 3);

-- Insert rentals by users on courts
INSERT INTO rentals(date, duration, user_id, court_id)
VALUES
    (TIMESTAMP '2025-08-01 10:00:00', 1, 1, 1),
    (TIMESTAMP '2025-08-02 14:00:00', 1, 2, 2),
    (TIMESTAMP '2025-08-03 16:00:00', 2, 3, 3);
