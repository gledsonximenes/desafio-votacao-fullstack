import React, { useEffect, useState, useCallback } from 'react';
import {
  Container,
  Typography,
  Paper,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  CircularProgress,
  Alert,
  Button,
  IconButton,
  Stack,
  Box
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import HowToVoteIcon from '@mui/icons-material/HowToVote';
import EventIcon from '@mui/icons-material/Event';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { ErroAPI } from '../types/ErroAPI';

interface Pauta { id: number; assunto: string; }

const PautaList: React.FC = () => {
  const [pautas, setPautas] = useState<Pauta[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const fetchData = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      // busca pautas e sessões
      const [pautaRes] = await Promise.all([
        api.get<Pauta[]>('/pautas'),
      ]);
      setPautas(pautaRes.data);

    } catch (err: unknown) {
      const apiErr = err as ErroAPI;
      setError(apiErr.mensagem);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchData(); }, [fetchData]);

  const goToVote = (id: number) => navigate(`/votos?pautaId=${id}`);
  const goToSession = (id: number) => navigate(`/sessoes/abrir?pautaId=${id}`);
  const goToCreate = () => navigate('/pautas/nova');

  const handleExcluir = async (id: number) => {
    if (!window.confirm('Deseja excluir esta pauta?')) return;
    try {
      await api.delete(`/pautas/${id}`);
      await fetchData();
    } catch (err: unknown) {
      const apiErr = err as ErroAPI;
      setError(apiErr.mensagem);
    }
  };

  return (
    <Container maxWidth="md" sx={{ mt: 4 }}>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
        <Typography variant="h4">Pautas</Typography>
        <Button variant="contained" onClick={goToCreate}>Adicionar Nova Pauta</Button>
      </Box>

      {loading ? <CircularProgress />
      : error ? (
        <Alert severity="error" action={
          <Button color="inherit" size="small" onClick={fetchData}>Tentar Novamente</Button>
        }>{error}</Alert>
      ) : pautas.length === 0 ? (
        <Alert severity="info">Nenhuma pauta cadastrada.</Alert>
      ) : (
        <Paper>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Assunto</TableCell>
                <TableCell align="center">Ações</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {pautas.map(p => (
                <TableRow key={p.id} hover>
                  <TableCell>{p.id}</TableCell>
                  <TableCell>{p.assunto}</TableCell>
                  <TableCell align="center">
                    <Stack direction="row" spacing={1} justifyContent="center">
                      {/* Votar */}
                      <IconButton onClick={() => goToVote(p.id)} title="Votar">
                        <HowToVoteIcon />
                      </IconButton>
                      {/* Abrir Sessão */}
                      <IconButton onClick={() => goToSession(p.id)} title="Abrir Sessão">
                        <EventIcon />
                      </IconButton>
                      {/* Excluir */}
                      <IconButton color="error" onClick={() => handleExcluir(p.id)} title="Excluir">
                        <DeleteIcon />
                      </IconButton>
                    </Stack>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Paper>
      )}
    </Container>
  );
};

export default PautaList;
