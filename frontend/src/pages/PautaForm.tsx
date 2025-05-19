import React, { useState } from 'react';
import { Container, Typography, TextField, Button, Alert, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { ErroAPI } from '../types/ErroAPI';

const PautaForm: React.FC = () => {
  const [assunto, setAssunto] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!assunto.trim()) {
      setError('Assunto é obrigatório.');
      return;
    }
    setLoading(true);
    setError(null);
    try {
      await api.post('/pautas', { assunto });
      navigate('/');
    } catch (err: unknown) {
      if (typeof err === 'object' && err !== null && 'mensagem' in err) {
        const erro = err as ErroAPI;
        setError(erro.mensagem);
      } else {
        setError('Erro ao salvar a pauta.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="sm" sx={{ mt: 4 }}>
      <Typography variant="h5" gutterBottom>Criar Nova Pauta</Typography>
      <Box component="form" onSubmit={handleSubmit} noValidate sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
        {error && <Alert severity="error">{error}</Alert>}
        <TextField
          label="Assunto"
          value={assunto}
          onChange={e => setAssunto(e.target.value)}
          disabled={loading}
          fullWidth
        />
        <Button type="submit" variant="contained" color="primary" disabled={loading}>
          {loading ? 'Salvando...' : 'Salvar Pauta'}
        </Button>
      </Box>
    </Container>
  );
};

export default PautaForm;