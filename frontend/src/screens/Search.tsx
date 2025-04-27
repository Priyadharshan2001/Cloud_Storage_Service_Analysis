import { useEffect, useState } from 'react';
import SearchBar from '@/components/SearchBar';
import apiCall from '@/lib/apiCall';
import PlanList from '@/components/PlanList';
import MinMaxRangeSelector from '@/components/MinMaxRangeSelector';
import { Separator } from '@/components/ui/separator';
import { Button } from '@/components/ui/button';
import { extractCapacityValue, parseCapacity } from '@/lib/utils';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';

const Search = () => {
  const [currentTab, setCurrentTab] = useState('search');
  const [searchTerm, setSearchTerm] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const [spellCheck, setSpellCheck] = useState('');
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [list, setList] = useState([]);
  const [capacityRange, setCapacity] = useState({ min: null, max: null });
  const [capacityList, setCapacityList] = useState<
    { label: string; value: number }[]
  >([]);

  // Load capacity values for dropdown
  useEffect(() => {
    apiCall
      .get('/storage_list')
      .then(res => {
        const sortedList = res?.data?.data.map((e: any) => +e).sort((a, b) => a - b);
        setCapacityList(
          sortedList.map((e: number) => ({
            label: parseCapacity(e),
            value: e,
          }))
        );
      })
      .catch(err => console.error(err));
  }, []);

  // Autocomplete suggestions
  useEffect(() => {
    if (!searchTerm) {
      setSuggestions([]);
      return;
    }

    apiCall
      .get('/auto_complete', {
        params: { q: searchTerm },
      })
      .then(res => setSuggestions(res?.data?.data))
      .catch(err => console.error(err));
  }, [searchTerm]);

  // Search or filter logic
  const onSearch = (query?: string) => {
    apiCall
      .get('/search', {
        params:
          currentTab === 'search'
            ? { q: query || searchTerm }
            : { minStorage: capacityRange.min, maxStorage: capacityRange.max },
      })
      .then(res => {
        const data = res?.data?.data || [];

        const sorted = data.sort(
          (a: any, b: any) =>
            extractCapacityValue(a.Capacity) - extractCapacityValue(b.Capacity)
        );

        setList(sorted);
        setSpellCheck(res?.data?.spellCheck);
      })
      .catch(err => console.error(err))
      .finally(() => {
        setSuggestions([]);
        setShowSuggestions(false);
      });
  };

  return (
    <div className="p-4 bg-white dark:bg-gray-900 min-h-screen">
      <Tabs
        defaultValue="search"
        onValueChange={(tab) => {
          setCurrentTab(tab);
          setList([]); // ✅ Clear results on tab switch (both ways)
        }}
        className="flex flex-col"
      >
        <TabsList className="mb-2 self-center bg-gray-100 dark:bg-gray-800">
          <TabsTrigger
            value="search"
            className="data-[state=active]:bg-white data-[state=active]:dark:bg-gray-700"
          >
            Search
          </TabsTrigger>
          <TabsTrigger
            value="filter"
            className="data-[state=active]:bg-white data-[state=active]:dark:bg-gray-700"
          >
            Filter
          </TabsTrigger>
        </TabsList>

        {/* Search Tab */}
        <TabsContent value="search">
          <SearchBar
            data={suggestions}
            value={searchTerm}
            setValue={setSearchTerm}
            onSearch={onSearch}
            showSuggestions={showSuggestions}
            setShowSuggestions={setShowSuggestions}
          />
        </TabsContent>

        {/* Filter Tab */}
        <TabsContent value="filter">
          <div className="flex flex-row justify-center gap-5 items-center">
            <MinMaxRangeSelector
              label="Select Capacity"
              setValue={setCapacity}
              value={capacityRange}
              minList={capacityList.filter(e =>
                capacityRange.max ? e.value <= capacityRange.max : true
              )}
              maxList={capacityList.filter(e =>
                capacityRange.min ? e.value >= capacityRange.min : true
              )}
            />
            <Button onClick={() => onSearch()}>Filter</Button>
          </div>
        </TabsContent>
      </Tabs>

      {!!spellCheck && (
        <div className="mb-3 text-black dark:text-white">
          Do you mean:{' '}
          <a
            className="underline cursor-pointer text-blue-600 dark:text-blue-400"
            onClick={() => {
              setSearchTerm(spellCheck);
              onSearch(spellCheck);
            }}
          >
            {spellCheck}
          </a>
          ?
        </div>
      )}

      {(!!list.length && currentTab === 'filter') && (
        <div className="mb-3 text-3xl font-bold text-black dark:text-white">
          Our Recommendations:
        </div>
      )}

      <Separator orientation="horizontal" className="my-3 bg-gray-300 dark:bg-gray-700" />

      <div className="flex flex-col flex-1 align-middle mx-[10%]">
        <PlanList data={list} />
      </div>
    </div>
  );
};

export default Search;
