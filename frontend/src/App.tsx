import { ThemeProvider } from '@/components/theme-provider';
import { BrowserRouter, Routes, Route } from 'react-router';
import Search from '@/screens/Search';
import Home from './screens/Home';
import NavBar from './components/NavBar';
/*import FrequencyCount from './screens/FrequencyCount';*/
import History from './screens/History';
import { Toaster } from './components/ui/toaster';
import DataScraper from './screens/DataScraper';
import Subscribe from './screens/Subscribe';
import AboutUs from './screens/AboutUs';

const App = () => {
  return (
    <ThemeProvider defaultTheme="light" storageKey="vite-ui-theme">
      <BrowserRouter>
        <NavBar />
        <div className="ml-[260px] p-5"> {/* Ensures content is not hidden behind sidebar */}
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/search" element={<Search />} />
            {/* <Route path="/frequency" element={<FrequencyCount />} /> */}
            <Route path="/search_history" element={<History />} />
            <Route path="/data_scraper" element={<DataScraper />} />
            <Route path="/subscribe" element={<Subscribe />} />
            <Route path="/AboutUs" element={<AboutUs />} />
          </Routes>
        </div>
        <Toaster />
      </BrowserRouter>
    </ThemeProvider>
  );
};

export default App;
