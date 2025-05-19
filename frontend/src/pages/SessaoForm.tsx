import React, { useEffect, useState } from 'react';
import {
  Container, Typography, FormControl, InputLabel, Select,
  MenuItem, TextField, Button, Alert, CircularProgress, Box
} from '@mui/material';
import { useNavigate, useSearchParams } from 'react-router-dom';
import api from '../services/api';
import { ErroAPI } from '../types/ErroAPI';

interface Pauta { id: number; assunto: string; }

const SessaoForm: React.FC = () => {
  const [searchParams] = useSearchParams();
  const [pautas, setPautas] = useState<Pauta[]>([]);
  const [pautaId, setPautaId] = useState<number | ''>('');
  const [duracao, setDuracao] = useState<number>(1);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  // Preenche pautaId se veio na URL
  useEffect(() => {
    const id = searchParams.get('pautaId');
    if (id) setPautaId(Number(id));
  }, [searchParams]);

  useEffect(() => {
    api.get<Pauta[]>('/pautas')
      .then(res => setPautas(res.data))
      .catch((err: ErroAPI) => setError(err.mensagem))
      .finally(() => setLoading(false));
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!pautaId) { setError('Selecione uma pauta'); return; }
    setLoading(true);
    setError(null);
    try {
      await api.post('/sessoes', { pautaId, duracao: `PT${duracao}M` });
      navigate(`/resultado?pautaId=${pautaId}`);
    } catch (err: unknown) {
      setError((err as ErroAPI).mensagem);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="sm" sx={{ mt: 4 }}>
      <Typography variant="h5" gutterBottom>Abrir Sessão</Typography>
      {loading ? <CircularProgress /> : (
        <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
          {error && <Alert severity="error">{error}</Alert>}
          <FormControl fullWidth>
            <InputLabel>Pauta</InputLabel>
            <Select value={pautaId} label="Pauta" onChange={e => setPautaId(Number(e.target.value))}>
              <MenuItem value="">-- Selecione --</MenuItem>
              {pautas.map(p => <MenuItem key={p.id} value={p.id}>{p.assunto}</MenuItem>)}
            </Select>
          </FormControl>
          <TextField label="Duração (min)" type="number" value={duracao} onChange={e => setDuracao(Number(e.target.value))} fullWidth />
          <Button type="submit" variant="contained">Abrir Sessão</Button>
        </Box>
      )}
    </Container>
  );
};

export default SessaoForm;