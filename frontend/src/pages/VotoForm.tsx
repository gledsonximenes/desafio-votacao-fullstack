import React, { useEffect, useState } from 'react';
import {
  Container, Typography, FormControl, InputLabel, Select,
  MenuItem, TextField, Button, Alert, Box, FormHelperText, RadioGroup, FormControlLabel, Radio
} from '@mui/material';
import { useNavigate, useSearchParams } from 'react-router-dom';
import api from '../services/api';
import { ErroAPI } from '../types/ErroAPI';

interface Pauta { id: number; assunto: string; }

const VotoForm: React.FC = () => {
  const [searchParams] = useSearchParams();
  const [pautas, setPautas] = useState<Pauta[]>([]);
  const [pautaId, setPautaId] = useState<string>('');
  const [associadoId, setAssociadoId] = useState<string>('');
  const [voto, setVoto] = useState<'SIM'|'NAO'>('SIM');
  const [errors, setErrors] = useState<{ pautaId?: string; associadoId?: string; voto?: string }>({});
  const [serverError, setServerError] = useState<string|null>(null);
  const navigate = useNavigate();

  // Pega pautaId da URL
  useEffect(() => {
    const id = searchParams.get('pautaId');
    if (id) setPautaId(id);
  }, [searchParams]);

  useEffect(() => {
    api.get<Pauta[]>('/pautas')
      .then(res => setPautas(res.data))
      .catch((err: ErroAPI) => setServerError(err.mensagem));
  }, []);

  const maskCPF = (val: string) => {
    let v = val.replace(/\D/g, '');
    if (v.length>11) v=v.slice(0,11);
    const p1=v.slice(0,3), p2=v.slice(3,6), p3=v.slice(6,9), p4=v.slice(9,11);
    return `${p1}${p2?'.'+p2:''}${p3?'.'+p3:''}${p4?'-'+p4:''}`;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault(); setErrors({}); setServerError(null);
    const err: typeof errors = {};
    if (!pautaId) err.pautaId='Selecione uma pauta';
    const rawCpf=associadoId.replace(/\D/g,'');
    if(!rawCpf) err.associadoId='CPF obrigatório'; else if(rawCpf.length!==11) err.associadoId='CPF deve ter 11 dígitos';
    if(!voto) err.voto='Selecione voto';
    if(Object.keys(err).length){setErrors(err);return;}
    try{
      await api.post('/votos',{ pautaId:Number(pautaId), associadoId:rawCpf, voto });
      navigate(`/resultado?pautaId=${pautaId}`);
    }catch(err:unknown){setServerError((err as ErroAPI).mensagem);}    
  };

  return (
    <Container maxWidth="sm" sx={{mt:4}}>
      <Typography variant="h5" gutterBottom>Registrar Voto</Typography>
      <Box component="form" onSubmit={handleSubmit} sx={{display:'flex',flexDirection:'column',gap:2}}>
        {serverError&&<Alert severity="error">{serverError}</Alert>}
        <FormControl fullWidth error={!!errors.pautaId}>
          <InputLabel>Pauta</InputLabel>
          <Select value={pautaId} label="Pauta" onChange={e=>setPautaId(e.target.value)}>
            <MenuItem value="">--Selecione--</MenuItem>
            {pautas.map(p=><MenuItem key={p.id} value={String(p.id)}>{p.assunto}</MenuItem>)}
          </Select>
          <FormHelperText>{errors.pautaId}</FormHelperText>
        </FormControl>
        <TextField label="CPF Associado" fullWidth value={associadoId} onChange={e=>setAssociadoId(maskCPF(e.target.value))} error={!!errors.associadoId} helperText={errors.associadoId} inputProps={{maxLength:14}} />
        <FormControl component="fieldset" error={!!errors.voto}>
          <RadioGroup row value={voto} onChange={e=>setVoto(e.target.value as any)}>
            <FormControlLabel value="SIM" control={<Radio />} label="Sim" />
            <FormControlLabel value="NAO" control={<Radio />} label="Não" />
          </RadioGroup>
          <FormHelperText>{errors.voto}</FormHelperText>
        </FormControl>
        <Button type="submit" variant="contained">Votar</Button>
      </Box>
    </Container>
  );
};

export default VotoForm;
