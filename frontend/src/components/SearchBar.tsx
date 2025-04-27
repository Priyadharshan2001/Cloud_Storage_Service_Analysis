import { useEffect, useState } from 'react';
import {
  Command,
  CommandEmpty,
  CommandList,
  CommandInput,
  CommandGroup,
  CommandItem,
} from './ui/command';
import { Button } from './ui/button';

export type SearchBarProps = {
  data: Array<string>;
  value: string;
  setValue: (value: string) => void;
  onSearch: () => void;
  showSuggestions: boolean;
  setShowSuggestions: (value: boolean) => void;
};

const SearchBar = ({
  data = [],
  value = '',
  setValue,
  onSearch = () => {},
  showSuggestions = false,
  setShowSuggestions = () => {},
}: SearchBarProps) => {
  const [debounceText, setDebounceText] = useState(value);
  const [recentSearches, setRecentSearches] = useState<string[]>([]);
  const [filteredSuggestions, setFilteredSuggestions] = useState<string[]>([]);

  useEffect(() => {
    const timeout = setTimeout(() => setValue(debounceText), 300);
    return () => clearTimeout(timeout);
  }, [debounceText]);

  useEffect(() => {
    setDebounceText(value);
  }, [value]);

  const handleFocus = () => {
    const saved = JSON.parse(localStorage.getItem('recentSearches') || '[]');
    setRecentSearches(saved);
    setFilteredSuggestions(saved);
    setShowSuggestions(true);
  };

  useEffect(() => {
    if (!showSuggestions) return;
    const input = debounceText.toLowerCase();
    const liveSuggestions = recentSearches.filter(s =>
      s.toLowerCase().includes(input)
    );
    setFilteredSuggestions(liveSuggestions);
  }, [debounceText, recentSearches, showSuggestions]);

  const handleBlur = () => {
    setTimeout(() => setShowSuggestions(false), 200);
  };

  const handleSearchClick = () => {
    const term = debounceText.trim();
    if (!term) return;
    const updated = [term, ...recentSearches.filter(t => t !== term)].slice(0, 5);
    localStorage.setItem('recentSearches', JSON.stringify(updated));
    setRecentSearches(updated);
    setValue(term);
    onSearch();
  };

  return (
    <div className="flex flex-row gap-5 align-middle">
      <Command
        onFocus={handleFocus}
        onBlur={handleBlur}
        className="rounded-lg border shadow-md md:min-w-[450px] h-auto max-h-52"
      >
        <CommandInput
          placeholder="Search..."
          value={debounceText}
          onValueChange={setDebounceText}
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              e.preventDefault();
              handleSearchClick();
            }
          }}
        />
        <CommandList style={showSuggestions ? {} : { display: 'none' }}>
          {filteredSuggestions.length === 0 ? (
            <CommandEmpty>No results found.</CommandEmpty>
          ) : (
            <CommandGroup heading="Suggestions">
              {filteredSuggestions.map((text) => (
                <CommandItem
                  key={text}
                  onSelect={(e) => {
                    setDebounceText(e);
                    setValue(e);
                    setShowSuggestions(false);
                    onSearch(); // ✅ Trigger search on suggestion select
                  }}
                >
                  {text}
                </CommandItem>
              ))}
            </CommandGroup>
          )}
        </CommandList>
      </Command>

      <Button onClick={handleSearchClick}>Search</Button>
    </div>
  );
};

export default SearchBar;
