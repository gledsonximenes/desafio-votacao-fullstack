import React from 'react';
import { Routes, Route, Link } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Container, createTheme, ThemeProvider } from '@mui/material';
import { purple } from '@mui/material/colors';
import PautaList from './pages/PautaList';
import PautaForm from './pages/PautaForm';
import SessaoForm from './pages/SessaoForm';
import VotoForm from './pages/VotoForm';
import Resultado from './pages/Resultado';

const theme = createTheme({
  palette: {
    primary: { main: purple[500] },
    secondary: { main: '#ff9800' },
  },
});

const App: React.FC = () => (
  <ThemeProvider theme={theme}>
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Cooperativa de Votações
        </Typography>
        <Button color="inherit" component={Link} to="/">Pautas</Button>
        <Button color="inherit" component={Link} to="/sessoes/abrir">Abrir Sessão</Button>
        <Button color="inherit" component={Link} to="/votos">Votar</Button>
        <Button color="inherit" component={Link} to="/resultado">Resultado</Button>
      </Toolbar>
    </AppBar>
    <Container maxWidth="lg" sx={{ mt: 4 }}>
      <Routes>
        <Route path="/" element={<PautaList />} />
        <Route path="/pautas/nova" element={<PautaForm />} />
        <Route path="/sessoes/abrir" element={<SessaoForm />} />
        <Route path="/votos" element={<VotoForm />} />
        <Route path="/resultado" element={<Resultado />} />
      </Routes>
    </Container>
  </ThemeProvider>
);

export default App;