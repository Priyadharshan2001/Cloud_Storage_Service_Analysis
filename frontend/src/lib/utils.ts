import { clsx, type ClassValue } from 'clsx';
import { twMerge } from 'tailwind-merge';

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export const parseCapacity = (size: number) => {
  if (isNaN(size)) return null;
  try {
    if (size < 1024) return size.toFixed(2) + ' GB';
    return (size / 1024).toFixed(2) + ' TB';
  } catch (err) {
    console.error(err);
    return null;
  }
};

// Extract capacity value from strings like '1 TB', '500 GB', 'Unlimited'
export const extractCapacityValue = (capacity: string) => {
  if (!capacity) return 0;

  const cleaned = capacity.toLowerCase().replace(/\s/g, '');

  if (cleaned.includes('unlimited') || cleaned.includes('custom')) return 0;

  const num = parseFloat(cleaned);
  if (isNaN(num)) return 0;

  if (cleaned.includes('tb')) return num * 1024;
  if (cleaned.includes('gb')) return num;

  return num;
};

export const parsePrice = (value: string) => {
  if (!value || value.toLowerCase().includes('free')) return 0;
  const parsed = parseFloat(value.replace(/[^0-9.]/g, ''));
  return isNaN(parsed) ? 0 : parsed;
};
