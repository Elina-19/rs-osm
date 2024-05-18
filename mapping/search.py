import clickhouse_connect
import re
import Levenshtein as lev
import ngram
from jarowinkler import *

# Подключение к ClickHouse
client = clickhouse_connect.get_client(host='localhost',
                                       username='default',
                                       password='',
                                       database='rs_osm')


def split_name(name):
  model_name = ''.join(re.findall('[a-zA-Z0-9-]+', name))
  gen_name = ''.join(re.findall('[^a-zA-Z0-9-\\s]+', name))
  return model_name, gen_name


def model_eq(str1, str2):
  distance = lev.distance(str1, str2)
  max_len = max(len(str1), len(str2))
  return (max_len - distance) / max_len


def compare_names_query(str1, query, eq_func):
  model1, name1 = split_name(str1)
  model2, name2 = split_name(query)

  if not model2.strip():
    return eq_func(name1, name2)

  if not model1.strip():
    return eq_func(name1, name2)*0.2
  else:
    mod_eq = model_eq(model1, model2)
    name_eq = eq_func(name1, name2)
    return mod_eq*0.8 + name_eq*0.2


def compare_names(str1, str2, eq_func):
  model1, name1 = split_name(str1)
  model2, name2 = split_name(str2)

  if not model1.strip() or not model2.strip():
    return eq_func(name1, name2)
  else:
    mod_eq = model_eq(model1, model2)
    name_eq = eq_func(name1, name2)
    return mod_eq*0.8 + name_eq*0.2


def compare_strings_ngram(str1, str2):
  return ngram.NGram.compare(str1, str2, N=3)


def compare_strings_jaro(str1, str2):
  return jarowinkler_similarity(str1, str2)


def search_entities(query):
    sql_query = 'SELECT * FROM car_battery_processed'
    df = client.query_df(sql_query)
    df_ngram = df.copy()

    df_ngram['name_sim'] = df_ngram['name'].apply(compare_names_query, args=(query, compare_strings_ngram))
    df_ngram = df_ngram.sort_values('name_sim', ascending=False)

    df_filtered = df_ngram[df_ngram['name_sim'] > 0.1][['url', 'name', 'crawling_date']]
    df_filtered['crawling_date'] = df_filtered['crawling_date'].apply(lambda x: x.strftime('%Y-%m-%d %H:%M:%S'))
    result = df_filtered.to_dict(orient='records')

    # data = [
    #     {"url": "https://example.com/page1", "create_date": "2024-01-01"},
    #     {"url": "https://example.com/page2", "create_date": "2024-02-01"},
    #     {"url": "https://example.com/page3", "create_date": "2024-03-01"}
    # ]
    return result
